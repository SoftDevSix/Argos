package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.JavaStatementAnalyzer;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class NestedStatementAnalyzer implements ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext> {
    private final JavaStatementAnalyzer mainAnalyzer;

    @Override
    public ControlStructureAnalysis analyze(JavaParser.StatementContext node) {
        if (Optional.ofNullable(node).isEmpty()) {
            return buildEmptyAnalysis();
        }

        return analyzeNestedStatements(node);
    }

    private ControlStructureAnalysis analyzeNestedStatements(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        complexity += performAnalysis(node, this::analyzeBlockStatements, locations);
        complexity += performAnalysis(node, this::analyzeSwitchBlockStatements, locations);
        complexity += performAnalysis(node, this::analyzeRegularStatements, locations);

        return buildAnalysis(complexity, locations);
    }

    private int performAnalysis(JavaParser.StatementContext node,
                                Function<JavaParser.StatementContext, ControlStructureAnalysis> analysisFunction,
                                List<ComplexityLocation> locations) {
        ControlStructureAnalysis analysisResult = analysisFunction.apply(node);
        locations.addAll(analysisResult.getLocations());
        return analysisResult.getTotalComplexity();
    }

    private ControlStructureAnalysis analyzeBlockStatements(JavaParser.StatementContext node) {
        if (!StructureVerifier.hasBlockStatements(node)) {
            return buildEmptyAnalysis();
        }

        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (JavaParser.BlockStatementContext blockStmt : node.block().blockStatement()) {
            if (blockStmt.statement() != null) {
                complexity += mainAnalyzer.analyzeNode(blockStmt.statement());
                locations.addAll(mainAnalyzer.getComplexityLocation(blockStmt.statement()));
            }
        }

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeSwitchBlockStatements(JavaParser.StatementContext node) {
        if (StructureVerifier.hasSwitchStatement(node)) {
            return buildEmptyAnalysis();
        }

        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (JavaParser.SwitchBlockStatementGroupContext group : node.switchBlockStatementGroup()) {
            for (JavaParser.BlockStatementContext blockStmt : group.blockStatement()) {
                if (blockStmt.statement() != null) {
                    complexity += mainAnalyzer.analyzeNode(blockStmt.statement());
                    locations.addAll(mainAnalyzer.getComplexityLocation(blockStmt.statement()));
                }
            }
        }

        return buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeRegularStatements(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (JavaParser.StatementContext stmt : node.statement()) {
            if (!StructureVerifier.isElseIfStatement(node, stmt)) {
                complexity += mainAnalyzer.analyzeNode(stmt);
                locations.addAll(mainAnalyzer.getComplexityLocation(stmt));
            }
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
