package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ControlStructureAnalyzer implements ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext> {
    @Override
    public ControlStructureAnalysis analyze(JavaParser.StatementContext node) {
        if (Optional.ofNullable(node).isEmpty()) {
            return ControlStructureAnalysis.builder()
                    .totalComplexity(0)
                    .locations(new ArrayList<>())
                    .build();
        }

        return analyzeControlStructures(node);
    }

    private ControlStructureAnalysis analyzeControlStructures(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        complexity += performAnalysis(node, this::analyzeIfStatement, locations);
        complexity += performAnalysis(node, this::analyzeLoops, locations);
        complexity += performAnalysis(node, this::analyzeSwitchStatement, locations);
        complexity += performAnalysis(node, this::analyzeTryCatch, locations);

        return ControlStructureAnalysis.builder()
                .totalComplexity(complexity)
                .locations(locations)
                .build();
    }

    private int performAnalysis(JavaParser.StatementContext node,
                                Function<JavaParser.StatementContext, ControlStructureAnalysis> analysisFunction,
                                List<ComplexityLocation> locations) {
        ControlStructureAnalysis analysisResult = analysisFunction.apply(node);
        locations.addAll(analysisResult.getLocations());
        return analysisResult.getTotalComplexity();
    }

    private ControlStructureAnalysis analyzeIfStatement(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        if (StructureVerifier.hasIfStatement(node)) {
            return buildAnalysis(complexity, locations);
        }

        complexity++;
        locations.add(ComplexityLocationFactory.createIfLocation(node));

        if (StructureVerifier.hasElseIfStatement(node)) {
            complexity++;
            locations.add(ComplexityLocationFactory.createElseIfLocation(node));
        }

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeLoops(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        if (StructureVerifier.hasForLoop(node)) {
            complexity++;
            locations.add(ComplexityLocationFactory.createForLoopLocation(node));
        }

        if (StructureVerifier.hasWhileLoop(node)) {
            complexity++;
            locations.add(ComplexityLocationFactory.createWhileLoopLocation(node));
        }

        if (StructureVerifier.hasDoWhileLoop(node)) {
            complexity++;
            locations.add(ComplexityLocationFactory.createDoWhileLoopLocation(node));
        }

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeSwitchStatement(JavaParser.StatementContext node) {
        if (StructureVerifier.hasSwitchStatement(node)) {
            return buildEmptyAnalysis();
        }

        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (JavaParser.SwitchBlockStatementGroupContext group : node.switchBlockStatementGroup()) {
            for (JavaParser.SwitchLabelContext label : group.switchLabel()) {
                if (StructureVerifier.hasCaseLabel(label)) {
                    complexity++;
                    locations.add(ComplexityLocationFactory.createSwitchCaseLocation(label));
                }
            }
        }

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeTryCatch(JavaParser.StatementContext node) {
        if (StructureVerifier.hasTryBlock(node)) {
            return buildEmptyAnalysis();
        }

        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (JavaParser.CatchClauseContext catchClause : node.catchClause()) {
            complexity++;
            locations.add(ComplexityLocationFactory.createCatchLocation(catchClause));
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
