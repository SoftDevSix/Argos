package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class LogicalOperatorAnalyzer implements ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext> {
    @Override
    public ControlStructureAnalysis analyze(JavaParser.StatementContext node) {
        if (Optional.ofNullable(node).isEmpty()) {
            return buildEmptyAnalysis();
        }

        return analyzeExpressions(node);
    }

    private ControlStructureAnalysis analyzeExpressions(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        complexity += performAnalysis(node, this::analyzeParenthesisExpression, locations);
        complexity += performAnalysis(node, this::analyzeDoWhileExpression, locations);
        complexity += performAnalysis(node, this::analyzeGeneralExpressions, locations);

        return buildAnalysis(complexity, locations);
    }

    private int performAnalysis(JavaParser.StatementContext node,
                                Function<JavaParser.StatementContext, ControlStructureAnalysis> analysisFunction,
                                List<ComplexityLocation> locations) {
        ControlStructureAnalysis analysisResult = analysisFunction.apply(node);
        locations.addAll(analysisResult.getLocations());
        return analysisResult.getTotalComplexity();
    }

    private ControlStructureAnalysis analyzeParenthesisExpression(JavaParser.StatementContext node) {
        if (!StructureVerifier.hasValidParenthesisExpression(node)) {
            return buildEmptyAnalysis();
        }

        String expressionText = node.parExpression().expression().getText();
        return analyzeOperatorsInExpression(expressionText, node.parExpression().getStart().getLine());
    }

    private ControlStructureAnalysis analyzeDoWhileExpression(JavaParser.StatementContext node) {
        if (!StructureVerifier.hasValidDoWhileExpression(node)) {
            return buildEmptyAnalysis();
        }

        String expressionText = node.parExpression().expression().getText();
        return analyzeOperatorsInExpression(expressionText, node.parExpression().getStart().getLine());
    }

    private ControlStructureAnalysis analyzeGeneralExpressions(JavaParser.StatementContext node) {
        if (!StructureVerifier.hasExpressions(node)) {
            return buildEmptyAnalysis();
        }

        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (JavaParser.ExpressionContext expr : node.expression()) {
            ControlStructureAnalysis expressionAnalysis = analyzeOperatorsInExpression(
                    expr.getText(),
                    expr.getStart().getLine()
            );
            complexity += expressionAnalysis.getTotalComplexity();
            locations.addAll(expressionAnalysis.getLocations());
        }

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeOperatorsInExpression(String text, int lineNumber) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        ControlStructureAnalysis andAnalysis = analyzeAndOperators(text, lineNumber);
        complexity += andAnalysis.getTotalComplexity();
        locations.addAll(andAnalysis.getLocations());

        ControlStructureAnalysis orAnalysis = analyzeOrOperators(text, lineNumber);
        complexity += orAnalysis.getTotalComplexity();
        locations.addAll(orAnalysis.getLocations());

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeAndOperators(String text, int lineNumber) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;
        int lastIndex = 0;

        while ((lastIndex = text.indexOf(MessagesAnalyzer.Operators.AND, lastIndex)) != -1) {
            complexity++;
            locations.add(ComplexityLocationFactory.createLogicalAndLocation(lineNumber, text));
            lastIndex += 2;
        }

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeOrOperators(String text, int lineNumber) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;
        int lastIndex = 0;

        while ((lastIndex = text.indexOf(MessagesAnalyzer.Operators.OR, lastIndex)) != -1) {
            complexity++;
            locations.add(ComplexityLocationFactory.createLogicalOrLocation(lineNumber, text));
            lastIndex += 2;
        }

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis buildAnalysis(int complexity, List<ComplexityLocation> locations) {
        return ControlStructureAnalysis.builder()
                .totalComplexity(complexity)
                .locations(locations)
                .build();
    }

    private ControlStructureAnalysis buildEmptyAnalysis() {
        return buildAnalysis(0, new ArrayList<>());
    }
}
