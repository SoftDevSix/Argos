package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ControlStructureAnalyzer implements ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext> {
    @Override
    public ControlStructureAnalysis analyze(JavaParser.StatementContext node) {
        if (Optional.ofNullable(node).isEmpty()) {
            return AnalyzerUtils.buildEmptyAnalysis();
        }

        return AnalyzerUtils.analyzeStructures(node, Arrays.asList(
                this::analyzeIfStatement,
                this::analyzeLoops,
                this::analyzeSwitchStatement,
                this::analyzeTryCatch
        ));
    }

    private ControlStructureAnalysis analyzeIfStatement(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        if (StructureVerifier.hasIfStatement(node)) {
            return AnalyzerUtils.buildAnalysis(complexity, locations);
        }

        complexity++;
        locations.add(ComplexityLocationFactory.createIfLocation(node));

        if (StructureVerifier.hasElseIfStatement(node)) {
            complexity++;
            locations.add(ComplexityLocationFactory.createElseIfLocation(node));
        }

        return AnalyzerUtils.buildAnalysis(complexity, locations);
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

        return AnalyzerUtils.buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeSwitchStatement(JavaParser.StatementContext node) {
        if (StructureVerifier.hasSwitchStatement(node)) {
            return AnalyzerUtils.buildEmptyAnalysis();
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

        return AnalyzerUtils.buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeTryCatch(JavaParser.StatementContext node) {
        if (StructureVerifier.hasTryBlock(node)) {
            return AnalyzerUtils.buildEmptyAnalysis();
        }

        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (JavaParser.CatchClauseContext catchClause : node.catchClause()) {
            complexity++;
            locations.add(ComplexityLocationFactory.createCatchLocation(catchClause));
        }

        return AnalyzerUtils.buildAnalysis(complexity, locations);
    }
}
