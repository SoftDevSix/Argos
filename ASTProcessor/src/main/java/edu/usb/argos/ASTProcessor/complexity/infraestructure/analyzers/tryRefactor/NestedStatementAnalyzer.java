package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.JavaStatementAnalyzer;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class NestedStatementAnalyzer implements ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext> {
    private final JavaStatementAnalyzer mainAnalyzer;

    @Override
    public ControlStructureAnalysis analyze(JavaParser.StatementContext node) {
        if (Optional.ofNullable(node).isEmpty()) {
            return AnalyzerUtils.buildEmptyAnalysis();
        }

        return AnalyzerUtils.analyzeStructures(node, Arrays.asList(
                this::analyzeBlockStatements,
                this::analyzeSwitchBlockStatements,
                this::analyzeRegularStatements
        ));
    }

    private ControlStructureAnalysis analyzeBlockStatements(JavaParser.StatementContext node) {
        if (!StructureVerifier.hasBlockStatements(node)) {
            return AnalyzerUtils.buildEmptyAnalysis();
        }

        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (JavaParser.BlockStatementContext blockStmt : node.block().blockStatement()) {
            if (blockStmt.statement() != null) {
                complexity += mainAnalyzer.analyzeNode(blockStmt.statement());
                locations.addAll(mainAnalyzer.getComplexityLocation(blockStmt.statement()));
            }
        }

        return AnalyzerUtils.buildAnalysis(complexity, locations);
    }

    private ControlStructureAnalysis analyzeSwitchBlockStatements(JavaParser.StatementContext node) {
        if (StructureVerifier.hasSwitchStatement(node)) {
            return AnalyzerUtils.buildEmptyAnalysis();
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

        return AnalyzerUtils.buildAnalysis(complexity, locations);
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

        return AnalyzerUtils.buildAnalysis(complexity, locations);
    }
}
