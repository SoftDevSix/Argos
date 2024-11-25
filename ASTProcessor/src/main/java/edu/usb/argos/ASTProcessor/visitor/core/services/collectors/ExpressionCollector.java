package edu.usb.argos.ASTProcessor.visitor.core.services.collectors;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IStatementCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpressionCollector implements
        IStatementCollector<JavaParser.StatementContext, JavaParser.MethodDeclarationContext> {
    @Override
    public List<JavaParser.StatementContext> collectStatements(JavaParser.MethodDeclarationContext ctx) {
        List<JavaParser.StatementContext> statements = new ArrayList<>();

        Optional.ofNullable(ctx)
                .map(JavaParser.MethodDeclarationContext::methodBody)
                .map(JavaParser.MethodBodyContext::block)
                .ifPresent(block -> collectStatementsRecursive(block, statements));

        return statements;
    }

    private void collectStatementsRecursive(JavaParser.BlockContext block,
                                            List<JavaParser.StatementContext> statements) {
        if (block == null || block.blockStatement() == null) {
            return;
        }

        for (JavaParser.BlockStatementContext blockStatement : block.blockStatement()) {
            processBlockStatement(blockStatement, statements);
        }
    }

    private void processBlockStatement(JavaParser.BlockStatementContext blockStatement,
                                       List<JavaParser.StatementContext> statements) {
        Optional.ofNullable(blockStatement.statement())
                .ifPresent(statement -> processStatement(statement, statements));
    }

    private void processStatement(JavaParser.StatementContext statement,
                                  List<JavaParser.StatementContext> statements) {
        if (statement.blockLabel != null) {
            collectStatementsRecursive(statement.blockLabel, statements);
            return;
        }

        statements.add(statement);

        Optional.ofNullable(statement.block())
                .ifPresent(block -> collectStatementsRecursive(block, statements));
    }
}
