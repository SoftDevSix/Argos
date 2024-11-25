package edu.usb.argos.ASTProcessor.bestpractices.analyzer;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedValueMatcher;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BlockAnalyzer {
    private final HardcodedValueMatcher hardcodedValueMatcher;
    private static BlockAnalyzer instance;

    public static synchronized BlockAnalyzer getInstance() {
        if (instance == null) {
            instance = new BlockAnalyzer(HardcodedValueMatcher.getInstance());
        }
        return instance;
    }

    public void analyze(JavaParser.BlockContext block, HardcodedValueMatcher matcher, List<String> detectedValues) {
        if (block != null) {
            for (JavaParser.BlockStatementContext stmt : block.blockStatement()) {
                if (stmt.statement() != null && stmt.statement().expression() != null) {
                    analyzeExpression(stmt, detectedValues);
                }
                if (stmt.localVariableDeclaration() != null) {
                    analyzeVariableDeclaration(stmt.localVariableDeclaration(), matcher, detectedValues);
                }
                if (stmt.statement() != null) {
                    analyzeStatement(stmt.statement(), matcher, detectedValues);
                }
            }
        }
    }

    private void analyzeVariableDeclaration(JavaParser.LocalVariableDeclarationContext varDeclaration,
                                                   HardcodedValueMatcher matcher, List<String> detectedValues) {
        for (JavaParser.VariableDeclaratorContext declarator :
                varDeclaration.variableDeclarators().variableDeclarator()) {
            if (declarator.variableInitializer() != null) {
                String variableValue = declarator.variableInitializer().getText();
                if (matcher.isHardcoded(variableValue)) {
                    System.out.println("Hardcoded value local at line " + declarator.getStart().getLine() + ": " + variableValue);
                    detectedValues.add(variableValue);
                }
            }
        }
    }

    private void analyzeStatement(JavaParser.StatementContext stmt,
                                         HardcodedValueMatcher matcher, List<String> detectedValues) {
        if (stmt.expression() != null) {
            for (JavaParser.ExpressionContext expr : stmt.expression()) {
                if (expr.methodCall() != null) {
                    analyzeMethodCall(expr, matcher, detectedValues, stmt.getStart().getLine());
                }
            }
        }
    }

    private void analyzeMethodCall(JavaParser.ExpressionContext expressionContext,
                                          HardcodedValueMatcher matcher, List<String> detectedValues, int line) {
        if(expressionContext.methodCall() != null && !expressionContext.methodCall() .arguments().isEmpty()){
            String methodArgumentValue =
                    expressionContext.methodCall().arguments().getChild(1).getText();
                String[] arguments = methodArgumentValue.split("\\s*,\\s*");

                for (String argument : arguments) {
                    if (matcher.isHardcoded(argument)) {
                        System.out.println("Hardcoded value at line " + line + ": " + argument);
                        detectedValues.add(argument);
                    }
                }
        }
    }

    private void analyzeExpression(JavaParser.BlockStatementContext stmt, List<String> detectedValues){
        for (JavaParser.ExpressionContext expressionContext : stmt.statement().expression()) {
            var assignmentTree = expressionContext.getChild(2);
            if(assignmentTree != null){
                String assignmentValue = assignmentTree.getText();
                if(hardcodedValueMatcher.isHardcoded(assignmentValue)){
                    System.out.println("Hardcoded value at line " + stmt.getStart().getLine() + " " + assignmentValue);
                    detectedValues.add(assignmentValue);
                }
            }
        }
    }
}

