package edu.usb.argos.ASTProcessor.complexity.core.domain;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.ComplexityAnalyzerStrategy;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.utils.analyzers.AnalyzerUtils;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.utils.analyzers.StructureVerifier;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.utils.factories.ComplexityLocationFactory;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.utils.messages.MessagesAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LogicalOperatorAnalyzer implements ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext> {
    @Override
    public ControlStructureAnalysis analyze(JavaParser.StatementContext node) {
        if (Optional.ofNullable(node).isEmpty()) {
            return AnalyzerUtils.buildEmptyAnalysis();
        }

        return AnalyzerUtils.analyzeStructures(node, Arrays.asList(
                this::analyzeParenthesisExpression,
                this::analyzeDoWhileExpression,
                this::analyzeGeneralExpressions
        ));
    }

    private ControlStructureAnalysis analyzeParenthesisExpression(JavaParser.StatementContext node) {
        if (!StructureVerifier.hasValidParenthesisExpression(node)) {
            return AnalyzerUtils.buildEmptyAnalysis();
        }

        String expressionText = node.parExpression().expression().getText();
        return analyzeOperatorsInExpression(expressionText, node.parExpression().getStart().getLine());
    }

    private ControlStructureAnalysis analyzeDoWhileExpression(JavaParser.StatementContext node) {
        if (!StructureVerifier.hasValidDoWhileExpression(node)) {
            return AnalyzerUtils.buildEmptyAnalysis();
        }

        String expressionText = node.parExpression().expression().getText();
        return analyzeOperatorsInExpression(expressionText, node.parExpression().getStart().getLine());
    }

    private ControlStructureAnalysis analyzeGeneralExpressions(JavaParser.StatementContext node) {
        if (!StructureVerifier.hasExpressions(node)) {
            return AnalyzerUtils.buildEmptyAnalysis();
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

        return AnalyzerUtils.buildAnalysis(complexity, locations);
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

        return AnalyzerUtils.buildAnalysis(complexity, locations);
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

        return AnalyzerUtils.buildAnalysis(complexity, locations);
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

        return AnalyzerUtils.buildAnalysis(complexity, locations);
    }
}
