package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.NodeAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.factories.AnalyzerStrategyFactory;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.ComplexityAnalyzerStrategy;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JavaStatementAnalyzer implements NodeAnalyzer<JavaParser.StatementContext> {
    private final List<ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext>> strategies;

    public JavaStatementAnalyzer() {
        this.strategies = AnalyzerStrategyFactory.createStrategies(this);
    }

    @Override
    public int analyzeNode(JavaParser.StatementContext node) {
        if (Optional.ofNullable(node).isEmpty()) {
            return 0;
        }

        int totalComplexity = 0;
        for (ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext> strategy : strategies) {
            ControlStructureAnalysis analysis = strategy.analyze(node);
            totalComplexity += analysis.getTotalComplexity();
        }
        return totalComplexity;
    }

    @Override
    public List<ComplexityLocation> getComplexityLocation(JavaParser.StatementContext node) {
        if (Optional.ofNullable(node).isEmpty()) {
            return new ArrayList<>();
        }

        List<ComplexityLocation> allLocations = new ArrayList<>();
        for (ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext> strategy : strategies) {
            ControlStructureAnalysis analysis = strategy.analyze(node);
            allLocations.addAll(analysis.getLocations());
        }
        return allLocations;
    }
}
