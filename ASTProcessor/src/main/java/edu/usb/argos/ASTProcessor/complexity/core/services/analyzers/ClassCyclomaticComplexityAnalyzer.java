package edu.usb.argos.ASTProcessor.complexity.core.services.analyzers;

import java.util.ArrayList;
import java.util.List;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityResult;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CodeElementAdapter;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.ComplexityRulesManager;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClassCyclomaticComplexityAnalyzer implements CyclomaticComplexityAnalyzer<JavaParser.StatementContext, ComplexityResult> {
    private MethodCyclomaticComplexityAnalyzer methodAnalyzer;
    private ConstructorCyclomaticComplexityAnalyzer constructorAnalyzer;
    private ComplexityRulesManager rulesManager;
    private CodeElementAdapter<JavaParser.StatementContext> target;

    @Override
    public ComplexityResult analyze() {
        int score = calculateComplexityScore();
        return ComplexityResult.builder()
                .elementName(target.getName())
                .complexityScore(score)
                .complexityLevel(determineComplexityLevel(score))
                .isWithinLimits(isWithinComplexityLimits(score))
                .complexityLocations(calculateComplexityLocations())
                .build();
    }

    @Override
    public String getMetricName() {
        return "Class Cyclomatic Complexity";
    }

    @Override
    public int calculateComplexityScore() {
        int totalComplexity = 1;
        if(methodAnalyzer != null) {
            ComplexityResult methodResult = methodAnalyzer.analyze();
            totalComplexity += methodResult.getComplexityScore();
        }

        if(constructorAnalyzer != null) {
            ComplexityResult constructorResult = constructorAnalyzer.analyze();
            totalComplexity += constructorResult.getComplexityScore();
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

    private List<ComplexityLocation> calculateComplexityLocations() {
        List<ComplexityLocation> locations = new ArrayList<>();

        if(methodAnalyzer != null) {
            ComplexityResult methodResult = methodAnalyzer.analyze();
            locations.addAll(methodResult.getComplexityLocations());
        }

        if(constructorAnalyzer != null) {
            ComplexityResult constructorResult = constructorAnalyzer.analyze();
            locations.addAll(constructorResult.getComplexityLocations());
        }

        return locations;
    }

}
