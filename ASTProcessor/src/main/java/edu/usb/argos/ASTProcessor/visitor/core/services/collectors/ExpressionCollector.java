package edu.usb.argos.ASTProcessor.visitor.core.services.collectors;

import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IStatementCollector;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;

import java.util.ArrayList;
import java.util.List;

public class ExpressionCollector implements
        IStatementCollector<JavaParser.StatementContext, JavaParser.MethodDeclarationContext> {
    @Override
    public List<JavaParser.StatementContext> collectStatements(JavaParser.MethodDeclarationContext ctx) {
        List<JavaParser.StatementContext> statements = new ArrayList<>();
        if (ctx.methodBody() != null && ctx.methodBody().block() != null) {
            collectStatementsRecursive(ctx.methodBody().block(), statements);
        }
        return statements;
    }

    private void collectStatementsRecursive(JavaParser.BlockContext block,
                                            List<JavaParser.StatementContext> statements) {
        if (block == null || block.blockStatement() == null) return;

        for (JavaParser.BlockStatementContext blockStatement : block.blockStatement()) {
            if (blockStatement.statement() != null) {
                if (blockStatement.statement().blockLabel != null) {
                    collectStatementsRecursive(blockStatement.statement().blockLabel, statements);
                } else {
                    statements.add(blockStatement.statement());
                    if (blockStatement.statement().block() != null) {
                        collectStatementsRecursive(blockStatement.statement().block(), statements);
                    }
                }
            }
        }
    }
}
