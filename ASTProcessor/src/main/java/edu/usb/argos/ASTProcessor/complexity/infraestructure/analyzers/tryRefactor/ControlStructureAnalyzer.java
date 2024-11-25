package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;

import java.util.ArrayList;
import java.util.List;

public class ControlStructureAnalyzer implements ComplexityAnalyzerStrategy<JavaParser.StatementContext, ComplexityLocation> {
    @Override
    public int analyze(JavaParser.StatementContext node) {
        if (node == null) return 0;
        int complexity = 0;

        complexity += analyzeIfStatement(node);
        complexity += analyzeLoops(node);
        complexity += analyzeSwitchStatement(node);
        complexity += analyzeTryCatch(node);

        return complexity;
    }

    private int analyzeIfStatement(JavaParser.StatementContext node) {
        int complexity = 0;
        if (node.IF() != null) {
            complexity++;
            if (node.ELSE() != null && node.statement(1) != null && node.statement(1).IF() != null) {
                complexity++;
            }
        }
        return complexity;
    }

    private int analyzeLoops(JavaParser.StatementContext node) {
        int complexity = 0;
        if (node.FOR() != null) complexity++;
        if (node.WHILE() != null && node.DO() == null) complexity++;
        if (node.DO() != null) complexity++;
        return complexity;
    }

    private int analyzeSwitchStatement(JavaParser.StatementContext node) {
        if (node.SWITCH() != null) {
            return (int) node.switchBlockStatementGroup().stream()
                    .flatMap(group -> group.switchLabel().stream())
                    .filter(label -> label.CASE() != null)
                    .count();
        }
        return 0;
    }

    private int analyzeTryCatch(JavaParser.StatementContext node) {
        if (node.TRY() != null) {
            return node.catchClause().size();
        }
        return 0;
    }

    @Override
    public List<ComplexityLocation> getComplexityLocations(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        if (node == null) return locations;

        collectIfLocations(node, locations);
        collectLoopLocations(node, locations);
        collectSwitchLocations(node, locations);
        collectTryLocations(node, locations);

        return locations;
    }

    private void collectIfLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.IF() == null) return;

        locations.add(ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.IF_STATEMENT)
                .description("Conditional branch")
                .contextInfo("if condition: " + node.parExpression().getText())
                .build());

        if (node.ELSE() != null && node.statement(1) != null && node.statement(1).IF() != null) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(node.statement(1).getStart().getLine())
                    .complexityType(ComplexityType.IF_STATEMENT)
                    .description("Else-if branch")
                    .contextInfo("else-if condition: " + node.statement(1).parExpression().getText())
                    .build());
        }
    }

    private void collectLoopLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.FOR() != null) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(node.getStart().getLine())
                    .complexityType(ComplexityType.LOOP)
                    .description("For loop")
                    .contextInfo("for loop with control: " + node.forControl().getText())
                    .build());
        }

        if (node.WHILE() != null && node.DO() == null) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(node.getStart().getLine())
                    .complexityType(ComplexityType.LOOP)
                    .description("While loop")
                    .contextInfo("while condition: " + node.parExpression().getText())
                    .build());
        }

        if (node.DO() != null) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(node.getStart().getLine())
                    .complexityType(ComplexityType.LOOP)
                    .description("Do-while loop")
                    .contextInfo("do-while condition: " + node.parExpression().getText())
                    .build());
        }
    }

    private void collectSwitchLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.SWITCH() == null) return;

        node.switchBlockStatementGroup().forEach(group ->
                group.switchLabel().stream()
                        .filter(label -> label.CASE() != null)
                        .forEach(label -> locations.add(ComplexityLocation.builder()
                                .lineNumber(label.getStart().getLine())
                                .complexityType(ComplexityType.SWITCH_CASE)
                                .description("Switch case")
                                .contextInfo("case: " + label.getText())
                                .build()))
        );
    }

    private void collectTryLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.TRY() == null) return;

        node.catchClause().forEach(catchClause ->
                locations.add(ComplexityLocation.builder()
                        .lineNumber(catchClause.getStart().getLine())
                        .complexityType(ComplexityType.CATCH_BLOCK)
                        .description("Exception handling")
                        .contextInfo("catch block for: " + catchClause.catchType().getText())
                        .build())
        );
    }
}
