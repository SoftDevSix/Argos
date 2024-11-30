package edu.usb.argos.ASTProcessor.complexity.infraestructure.factories;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ControlStructureAnalysis;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.ComplexityAnalyzerStrategy;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.JavaStatementAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.domain.ControlStructureAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.domain.LogicalOperatorAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.domain.NestedStatementAnalyzer;

import java.util.Arrays;
import java.util.List;

public class AnalyzerStrategyFactory {
    public static List<ComplexityAnalyzerStrategy<ControlStructureAnalysis, JavaParser.StatementContext>> createStrategies(
            JavaStatementAnalyzer mainAnalyzer) {
        return Arrays.asList(
                new ControlStructureAnalyzer(),
                new LogicalOperatorAnalyzer(),
                new NestedStatementAnalyzer(mainAnalyzer)
        );
    }
}
