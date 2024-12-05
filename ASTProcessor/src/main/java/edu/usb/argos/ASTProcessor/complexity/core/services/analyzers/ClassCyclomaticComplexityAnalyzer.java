package edu.usb.argos.ASTProcessor.complexity.core.services.analyzers;

import java.util.ArrayList;
import java.util.List;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ClassComplexityResult;
import edu.usb.argos.ASTProcessor.complexity.core.entities.MethodComplexityInfo;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CodeElementAdapter;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.ComplexityRulesManager;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClassCyclomaticComplexityAnalyzer
        implements CyclomaticComplexityAnalyzer<JavaParser.StatementContext, ClassComplexityResult> {
    private List<MethodCyclomaticComplexityAnalyzer> methodAnalyzers;
    private List<ConstructorCyclomaticComplexityAnalyzer> constructorAnalyzers;
    private ComplexityRulesManager rulesManager;
    private CodeElementAdapter<JavaParser.StatementContext> target;

    @Override
    public ClassComplexityResult analyze() {
        int totalScore = calculateComplexityScore();
        ComplexityLevel overallLevel = determineComplexityLevel(totalScore);

        return ClassComplexityResult.builder()
                .className(target.getName())
                .totalComplexityScore(totalScore)
                .overallComplexityLevel(overallLevel)
                .isWithinLimits(isWithinComplexityLimits(totalScore))
                .methodResults(calculateMethodResults())
                .constructorResults(calculateConstructorResults())
                .build();
    }

    @Override
    public String getMetricName() {
        return "Class Cyclomatic Complexity";
    }

    @Override
    public int calculateComplexityScore() {
        int totalComplexity = 1;

        if (methodAnalyzers != null) {
            for (MethodCyclomaticComplexityAnalyzer methodAnalyzer : methodAnalyzers) {
                totalComplexity += methodAnalyzer.analyze().getComplexityScore();
            }
        }

        if (constructorAnalyzers != null) {
            for (ConstructorCyclomaticComplexityAnalyzer constructorAnalyzer : constructorAnalyzers) {
                totalComplexity += constructorAnalyzer.analyze().getComplexityScore();
            }
        }

        return totalComplexity;
    }

    @Override
    public ComplexityLevel determineComplexityLevel(int score) {
        return rulesManager.determineLevel(score);
    }

    @Override
    public boolean isWithinComplexityLimits(int score) {
        return !rulesManager.isComplexityLimitEnabled() || score <= rulesManager.getMaxComplexity();
    }

    private List<MethodComplexityInfo> calculateMethodResults() {
        List<MethodComplexityInfo> methodResults = new ArrayList<>();

        if (methodAnalyzers != null) {
            for (MethodCyclomaticComplexityAnalyzer methodAnalyzer : methodAnalyzers) {
                var methodAnalysis = methodAnalyzer.analyze();
                methodResults.add(MethodComplexityInfo.builder()
                        .methodName(methodAnalyzer.getTarget().getName())
                        .complexityScore(methodAnalysis.getComplexityScore())
                        .complexityLevel(methodAnalysis.getComplexityLevel())
                        .isWithinLimits(methodAnalysis.isWithinLimits())
                        .complexityLocations(new ArrayList<>(methodAnalysis.getComplexityLocations()))
                        .build());
            }
        }

        return methodResults;
    }

    private List<MethodComplexityInfo> calculateConstructorResults() {
        List<MethodComplexityInfo> constructorResults = new ArrayList<>();

        if (constructorAnalyzers != null) {
            for (ConstructorCyclomaticComplexityAnalyzer constructorAnalyzer : constructorAnalyzers) {
                var constructorAnalysis = constructorAnalyzer.analyze();
                constructorResults.add(MethodComplexityInfo.builder()
                        .methodName(constructorAnalyzer.getTarget().getName())
                        .complexityScore(constructorAnalysis.getComplexityScore())
                        .complexityLevel(constructorAnalysis.getComplexityLevel())
                        .isWithinLimits(constructorAnalysis.isWithinLimits())
                        .complexityLocations(new ArrayList<>(constructorAnalysis.getComplexityLocations()))
                        .build());
            }
        }

        return constructorResults;
    }
}