package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComplexityLocationFactory {
    public ComplexityLocation createIfLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.IF_STATEMENT)
                .description(ControlStructureMessages.Descriptions.IF)
                .contextInfo(String.format(ControlStructureMessages.ContextFormats.IF,
                        node.parExpression().getText()))
                .build();
    }

    public ComplexityLocation createElseIfLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.statement(1).getStart().getLine())
                .complexityType(ComplexityType.IF_STATEMENT)
                .description(ControlStructureMessages.Descriptions.ELSE_IF)
                .contextInfo(String.format(ControlStructureMessages.ContextFormats.ELSE_IF,
                        node.statement(1).parExpression().getText()))
                .build();
    }

    public ComplexityLocation createForLoopLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.LOOP)
                .description(ControlStructureMessages.Descriptions.FOR_LOOP)
                .contextInfo(String.format(ControlStructureMessages.ContextFormats.FOR,
                        node.forControl().getText()))
                .build();
    }

    public ComplexityLocation createWhileLoopLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.LOOP)
                .description(ControlStructureMessages.Descriptions.WHILE_LOOP)
                .contextInfo(String.format(ControlStructureMessages.ContextFormats.WHILE,
                        node.parExpression().getText()))
                .build();
    }

    public ComplexityLocation createDoWhileLoopLocation(JavaParser.StatementContext node) {
        return ComplexityLocation.builder()
                .lineNumber(node.getStart().getLine())
                .complexityType(ComplexityType.LOOP)
                .description(ControlStructureMessages.Descriptions.DO_WHILE)
                .contextInfo(String.format(ControlStructureMessages.ContextFormats.DO_WHILE,
                        node.parExpression().getText()))
                .build();
    }

    public ComplexityLocation createSwitchCaseLocation(JavaParser.SwitchLabelContext label) {
        return ComplexityLocation.builder()
                .lineNumber(label.getStart().getLine())
                .complexityType(ComplexityType.SWITCH_CASE)
                .description(ControlStructureMessages.Descriptions.SWITCH_CASE)
                .contextInfo(String.format(ControlStructureMessages.ContextFormats.CASE,
                        label.getText()))
                .build();
    }

    public ComplexityLocation createCatchLocation(JavaParser.CatchClauseContext catchClause) {
        return ComplexityLocation.builder()
                .lineNumber(catchClause.getStart().getLine())
                .complexityType(ComplexityType.CATCH_BLOCK)
                .description(ControlStructureMessages.Descriptions.EXCEPTION)
                .contextInfo(String.format(ControlStructureMessages.ContextFormats.CATCH,
                        catchClause.catchType().getText()))
                .build();
    }
}
