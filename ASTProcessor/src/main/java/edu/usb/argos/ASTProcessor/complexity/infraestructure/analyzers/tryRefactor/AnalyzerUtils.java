package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class AnalyzerUtils {
    public <T> ControlStructureAnalysis analyzeStructures(
            T node,
            List<Function<T, ControlStructureAnalysis>> analyzers) {

        List<ComplexityLocation> locations = new ArrayList<>();
        int complexity = 0;

        for (Function<T, ControlStructureAnalysis> analyzer : analyzers) {
            ControlStructureAnalysis analysisResult = performAnalysis(node, analyzer, locations);
            complexity += analysisResult.getTotalComplexity();
        }

        return buildAnalysis(complexity, locations);
    }

    private <T> ControlStructureAnalysis performAnalysis(
            T node,
            Function<T, ControlStructureAnalysis> analysisFunction,
            List<ComplexityLocation> locations) {

        ControlStructureAnalysis analysisResult = analysisFunction.apply(node);
        locations.addAll(analysisResult.getLocations());
        return analysisResult;
    }

    public ControlStructureAnalysis buildAnalysis(int complexity, List<ComplexityLocation> locations) {
        return ControlStructureAnalysis.builder()
                .totalComplexity(complexity)
                .locations(locations)
                .build();
    }

    public ControlStructureAnalysis buildEmptyAnalysis() {
        return buildAnalysis(0, new ArrayList<>());
    }
}
