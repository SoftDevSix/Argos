package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.analyzer;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedValueMatcher;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BlockAnalyzer {
    private final HardcodedValueMatcher matcher;
    private static BlockAnalyzer instance;

    public static synchronized BlockAnalyzer getInstance() {
        if (instance == null) {
            instance = new BlockAnalyzer(HardcodedValueMatcher.getInstance());
        }
        return instance;
    }

    public void analyze(JavaParser.BlockContext block, List<HardcodedDetection> detectedValues) {
        if (block != null) {
            for (JavaParser.BlockStatementContext stmt : block.blockStatement()) {
                if (stmt.statement() != null && stmt.statement().expression() != null) {
                    analyzeExpression(stmt, detectedValues);
                }
                if (stmt.localVariableDeclaration() != null) {
                    analyzeVariableDeclaration(stmt.localVariableDeclaration(), detectedValues);
                }
                if (stmt.statement() != null) {
                    analyzeStatement(stmt.statement(), detectedValues);
                }
            }
        }
    }

    private void analyzeVariableDeclaration(
            JavaParser.LocalVariableDeclarationContext varDeclaration, List<HardcodedDetection> detectedValues
    ) {
        for (JavaParser.VariableDeclaratorContext declarator :
                varDeclaration.variableDeclarators().variableDeclarator()) {
            if (declarator.variableInitializer() != null) {
                String variableValue = declarator.variableInitializer().getText();
                if (matcher.isHardcoded(variableValue)) {
                    HardcodedDetection detection = HardcodedDetection.builder().hardcodedValue(variableValue)
                            .lineNumber(declarator.getStart().getLine()).build();
                    detectedValues.add(detection);
                }
            }
        }
    }

    private void analyzeStatement(JavaParser.StatementContext stmt, List<HardcodedDetection> detectedValues) {
        if (stmt.expression() != null) {
            for (JavaParser.ExpressionContext expr : stmt.expression()) {
                if (expr.methodCall() != null) {
                    analyzeMethodCall(expr, detectedValues, stmt.getStart().getLine());
                }
            }
        }
    }

    private void analyzeMethodCall(
            JavaParser.ExpressionContext expressionContext, List<HardcodedDetection> detectedValues, int line
    ) {
        if(expressionContext.methodCall() != null && !expressionContext.methodCall() .arguments().isEmpty()){
            String methodArgumentValue =
                    expressionContext.methodCall().arguments().getChild(1).getText();
                String[] arguments = methodArgumentValue.split("\\s*,\\s*");

                for (String argument : arguments) {
                    if (matcher.isHardcoded(argument)) {
                        HardcodedDetection detection = HardcodedDetection.builder().hardcodedValue(argument)
                                .lineNumber(line).build();
                        detectedValues.add(detection);
                    }
                }
        }
    }

    private void analyzeExpression(JavaParser.BlockStatementContext stmt, List<HardcodedDetection> detectedValues){
        for (JavaParser.ExpressionContext expressionContext : stmt.statement().expression()) {
            var assignmentTree = expressionContext.getChild(2);
            if(assignmentTree != null){
                String assignmentValue = assignmentTree.getText();
                if(matcher.isHardcoded(assignmentValue)){
                    HardcodedDetection detection = HardcodedDetection.builder().hardcodedValue(assignmentValue)
                            .lineNumber(stmt.getStart().getLine()).build();
                    detectedValues.add(detection);
                }
            }
        }
    }
}

