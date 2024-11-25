package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;

import java.util.ArrayList;
import java.util.List;

public class LogicalOperatorAnalyzer implements ComplexityAnalyzerStrategy<JavaParser.StatementContext, ComplexityLocation> {
    @Override
    public int analyze(JavaParser.StatementContext node) {
        if (node == null) return 0;

        int complexity = 0;
        complexity += analyzeParenthesisExpression(node);
        complexity += analyzeDoWhileExpression(node);
        complexity += analyzeGeneralExpressions(node);

        return complexity;
    }

    private int analyzeParenthesisExpression(JavaParser.StatementContext node) {
        if (node.parExpression() != null && node.parExpression().expression() != null) {
            return countLogicalOperators(node.parExpression().expression().getText());
        }
        return 0;
    }

    private int analyzeDoWhileExpression(JavaParser.StatementContext node) {
        if (node.DO() != null && node.parExpression() != null) {
            return countLogicalOperators(node.parExpression().expression().getText());
        }
        return 0;
    }

    private int analyzeGeneralExpressions(JavaParser.StatementContext node) {
        int complexity = 0;
        if (node.expression() != null) {
            for (JavaParser.ExpressionContext expr : node.expression()) {
                complexity += countLogicalOperators(expr.getText());
            }
        }
        return complexity;
    }

    private int countLogicalOperators(String text) {
        return (text.split("&&").length - 1) + (text.split("\\|\\|").length - 1);
    }

    @Override
    public List<ComplexityLocation> getComplexityLocations(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        if (node == null) return locations;

        collectParenthesisExpressionLocations(node, locations);
        collectDoWhileExpressionLocations(node, locations);
        collectGeneralExpressionLocations(node, locations);

        return locations;
    }

    private void collectParenthesisExpressionLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.parExpression() == null || node.parExpression().expression() == null) return;
        collectOperatorsFromExpression(
                node.parExpression().expression(),
                node.parExpression().getStart().getLine(),
                locations
        );
    }

    private void collectDoWhileExpressionLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.DO() == null || node.parExpression() == null) return;
        collectOperatorsFromExpression(
                node.parExpression().expression(),
                node.parExpression().getStart().getLine(),
                locations
        );
    }

    private void collectGeneralExpressionLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.expression() == null) return;
        node.expression().forEach(expr ->
                collectOperatorsFromExpression(expr, expr.getStart().getLine(), locations)
        );
    }

    private void collectOperatorsFromExpression(JavaParser.ExpressionContext expr, int lineNumber,
                                                List<ComplexityLocation> locations) {
        String text = expr.getText();
        collectAndOperators(text, lineNumber, locations);
        collectOrOperators(text, lineNumber, locations);
    }

    private void collectAndOperators(String text, int lineNumber, List<ComplexityLocation> locations) {
        int lastIndex = 0;
        while ((lastIndex = text.indexOf("&&", lastIndex)) != -1) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(lineNumber)
                    .complexityType(ComplexityType.LOGICAL_AND)
                    .description("Logical AND operator")
                    .contextInfo("in expression: " + text)
                    .build());
            lastIndex += 2;
        }
    }

    private void collectOrOperators(String text, int lineNumber, List<ComplexityLocation> locations) {
        int lastIndex = 0;
        while ((lastIndex = text.indexOf("||", lastIndex)) != -1) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(lineNumber)
                    .complexityType(ComplexityType.LOGICAL_OR)
                    .description("Logical OR operator")
                    .contextInfo("in expression: " + text)
                    .build());
            lastIndex += 2;
        }
    }
}
