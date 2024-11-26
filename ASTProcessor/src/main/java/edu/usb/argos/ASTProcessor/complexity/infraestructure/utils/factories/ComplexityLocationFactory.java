package edu.usb.argos.ASTProcessor.complexity.infraestructure.utils.factories;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.utils.messages.MessagesAnalyzer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComplexityLocationFactory {
    public ComplexityLocation createIfLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.IF_STATEMENT)
                .description(MessagesAnalyzer.Descriptions.IF)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.IF,
                        node.parExpression().getText()))
                .build();
    }

    public ComplexityLocation createElseIfLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.statement(1).getStart().getLine())
                .complexityType(ComplexityType.IF_STATEMENT)
                .description(MessagesAnalyzer.Descriptions.ELSE_IF)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.ELSE_IF,
                        node.statement(1).parExpression().getText()))
                .build();
    }

    public ComplexityLocation createForLoopLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.LOOP)
                .description(MessagesAnalyzer.Descriptions.FOR_LOOP)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.FOR,
                        node.forControl().getText()))
                .build();
    }

    public ComplexityLocation createWhileLoopLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.LOOP)
                .description(MessagesAnalyzer.Descriptions.WHILE_LOOP)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.WHILE,
                        node.parExpression().getText()))
                .build();
    }

    public ComplexityLocation createDoWhileLoopLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.LOOP)
                .description(MessagesAnalyzer.Descriptions.DO_WHILE)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.DO_WHILE,
                        node.parExpression().getText()))
                .build();
    }

    public ComplexityLocation createSwitchCaseLocation(JavaParser.SwitchLabelContext label) {
        return ComplexityLocation.builder()
                .lineNumber(label.getStart().getLine())
                .complexityType(ComplexityType.SWITCH_CASE)
                .description(MessagesAnalyzer.Descriptions.SWITCH_CASE)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.CASE,
                        label.getText()))
                .build();
    }

    public ComplexityLocation createCatchLocation(JavaParser.CatchClauseContext catchClause) {
        return ComplexityLocation.builder()
                .lineNumber(catchClause.getStart().getLine())
                .complexityType(ComplexityType.CATCH_BLOCK)
                .description(MessagesAnalyzer.Descriptions.EXCEPTION)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.CATCH,
                        catchClause.catchType().getText()))
                .build();
    }

    public ComplexityLocation createLogicalAndLocation(int lineNumber, String expression) {
        return ComplexityLocation.builder()
                .lineNumber(lineNumber)
                .complexityType(ComplexityType.LOGICAL_AND)
                .description(MessagesAnalyzer.Descriptions.LOGICAL_AND)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.EXPRESSION, expression))
                .build();
    }

    public ComplexityLocation createLogicalOrLocation(int lineNumber, String expression) {
        return ComplexityLocation.builder()
                .lineNumber(lineNumber)
                .complexityType(ComplexityType.LOGICAL_OR)
                .description(MessagesAnalyzer.Descriptions.LOGICAL_OR)
                .contextInfo(String.format(MessagesAnalyzer.ContextFormats.EXPRESSION, expression))
                .build();
    }
}
