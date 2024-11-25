package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.JavaStatementAnalyzer;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class NestedStatementAnalyzer implements ComplexityAnalyzerStrategy<JavaParser.StatementContext, ComplexityLocation> {
    private final JavaStatementAnalyzer mainAnalyzer;

    @Override
    public int analyze(JavaParser.StatementContext node) {
        if (node == null) return 0;

        return analyzeBlockStatements(node) +
                analyzeSwitchBlockStatements(node) +
                analyzeRegularStatements(node);
    }

    private int analyzeBlockStatements(JavaParser.StatementContext node) {
        if (node.block() == null) return 0;

        return node.block().blockStatement().stream()
                .filter(stmt -> stmt.statement() != null)
                .mapToInt(stmt -> mainAnalyzer.analyzeNode(stmt.statement()))
                .sum();
    }

    private int analyzeSwitchBlockStatements(JavaParser.StatementContext node) {
        if (node.SWITCH() == null) return 0;

        return node.switchBlockStatementGroup().stream()
                .flatMap(group -> group.blockStatement().stream())
                .filter(stmt -> stmt.statement() != null)
                .mapToInt(stmt -> mainAnalyzer.analyzeNode(stmt.statement()))
                .sum();
    }

    private int analyzeRegularStatements(JavaParser.StatementContext node) {
        return node.statement().stream()
                .filter(stmt -> !(node.ELSE() != null && stmt.IF() != null))
                .mapToInt(mainAnalyzer::analyzeNode)
                .sum();
    }

    @Override
    public List<ComplexityLocation> getComplexityLocations(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        if (node == null) return locations;

        Stream.of(
                collectBlockLocations(node),
                collectSwitchBlockLocations(node),
                collectRegularStatementLocations(node)
        ).forEach(locations::addAll);

        return locations;
    }

    private List<ComplexityLocation> collectBlockLocations(JavaParser.StatementContext node) {
        if (node.block() == null) return new ArrayList<>();

        return node.block().blockStatement().stream()
                .filter(stmt -> stmt.statement() != null)
                .flatMap(stmt -> mainAnalyzer.getComplexityLocation(stmt.statement()).stream())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private List<ComplexityLocation> collectSwitchBlockLocations(JavaParser.StatementContext node) {
        if (node.SWITCH() == null) return new ArrayList<>();

        return node.switchBlockStatementGroup().stream()
                .flatMap(group -> group.blockStatement().stream())
                .filter(stmt -> stmt.statement() != null)
                .flatMap(stmt -> mainAnalyzer.getComplexityLocation(stmt.statement()).stream())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private List<ComplexityLocation> collectRegularStatementLocations(JavaParser.StatementContext node) {
        return node.statement().stream()
                .filter(stmt -> !(node.ELSE() != null && stmt.IF() != null))
                .flatMap(stmt -> mainAnalyzer.getComplexityLocation(stmt).stream())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
