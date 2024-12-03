package edu.usb.argos.ASTProcessor.bestpractices.analyzer;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedValueMatcher;
import lombok.AllArgsConstructor;

import java.util.List;

public class BlockAnalyzer {
    private final HardcodedValueMatcher matcher;
    private final String ARGUMENTS_SPLIT_REGEX = "\\s*,\\s*";

    public BlockAnalyzer(){
        matcher = HardcodedValueMatcher.getInstance();
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
                    HardcodedDetection detection =
                            buildHardcodedDetection(variableValue, declarator.getStart().getLine());
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
                String[] arguments = methodArgumentValue.split(ARGUMENTS_SPLIT_REGEX);

                for (String argument : arguments) {
                    if (matcher.isHardcoded(argument)) {
                        HardcodedDetection detection = buildHardcodedDetection(argument, line);
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
                    HardcodedDetection detection = buildHardcodedDetection(assignmentValue, stmt.getStart().getLine());
                    detectedValues.add(detection);
                }
            }
        }
    }

    private HardcodedDetection buildHardcodedDetection(String assignmentValue, int lineNumber) {
        return HardcodedDetection.builder().hardcodedValue(assignmentValue)
                .lineNumber(lineNumber).build();
    }
}

