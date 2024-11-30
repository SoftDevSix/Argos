package edu.usb.argos.ASTProcessor.complexity.infraestructure.utils.analyzers;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class StructureVerifier {
    public boolean hasIfStatement(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.IF()).isEmpty();
    }

    public boolean hasElseIfStatement(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.ELSE()).isPresent() &&
                Optional.ofNullable(node.statement(1)).isPresent() &&
                Optional.ofNullable(node.statement(1).IF()).isPresent();
    }

    public boolean hasForLoop(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.FOR()).isPresent();
    }

    public boolean hasWhileLoop(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.WHILE()).isPresent() &&
                Optional.ofNullable(node.DO()).isEmpty();
    }

    public boolean hasDoWhileLoop(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.DO()).isPresent();
    }

    public boolean hasSwitchStatement(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.SWITCH()).isEmpty();
    }

    public boolean hasTryBlock(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.TRY()).isEmpty();
    }

    public boolean hasCaseLabel(JavaParser.SwitchLabelContext label) {
        return Optional.ofNullable(label.CASE()).isPresent();
    }

    public boolean hasValidParenthesisExpression(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.parExpression())
                .map(JavaParser.ParExpressionContext::expression)
                .isPresent();
    }

    public boolean hasValidDoWhileExpression(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.DO()).isPresent() &&
                Optional.ofNullable(node.parExpression()).isPresent();
    }

    public boolean hasExpressions(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.expression()).isPresent();
    }

    public boolean hasBlockStatements(JavaParser.StatementContext node) {
        return Optional.ofNullable(node.block()).isPresent();
    }

    public boolean isElseIfStatement(JavaParser.StatementContext node,
                                     JavaParser.StatementContext statement) {
        return Optional.ofNullable(node.ELSE()).isPresent() &&
                Optional.ofNullable(statement.IF()).isPresent();
    }
}
