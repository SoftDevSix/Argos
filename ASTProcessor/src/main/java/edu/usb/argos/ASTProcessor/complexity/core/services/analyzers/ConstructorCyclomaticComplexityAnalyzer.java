package edu.usb.argos.ASTProcessor.complexity.core.services.analyzers;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityResult;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CodeElementAdapter;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.NodeAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.ComplexityRulesManager;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class ConstructorCyclomaticComplexityAnalyzer
        implements CyclomaticComplexityAnalyzer<JavaParser.StatementContext, ComplexityResult> {
    CodeElementAdapter<JavaParser.StatementContext> target;
    ComplexityRulesManager rulesManager;
    NodeAnalyzer<JavaParser.StatementContext> statementAnalyzer;

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
        return "Constructor Cyclomatic Complexity";
    }

    @Override
    public int calculateComplexityScore() {
        int complexity = 1;

        for (Statement<JavaParser.StatementContext> statement : target.getStatements()) {
            complexity += statementAnalyzer.analyzeNode(statement.getNode());
        }

        return complexity;
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

        for (Statement<JavaParser.StatementContext> statement : target.getStatements()) {
            locations.addAll(statementAnalyzer.getComplexityLocation(statement.getNode()));
        }

        return locations;
    }
}