package edu.usb.argos.ASTProcessor.visitor.domain.services.collectors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes.IClassMetricsCollector;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.CommonTokenStream;

public class JavaClassMetricsCollector implements IClassMetricsCollector<ParserRuleContext> {
    private final CommonTokenStream tokenStream;

    public JavaClassMetricsCollector(CommonTokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    @Override
    public int getTotalLines(ParserRuleContext ctx) {
        int startLine = ctx.getStart().getLine();
        int endLine = ctx.getStop().getLine();
        return endLine - startLine + 1;
    }

    @Override
    public int getCommentLines(ParserRuleContext ctx) {
        int commentLines = 0;

        for (var token : tokenStream.getTokens()) {
            int tokenType = token.getType();

            if (tokenType == JavaParser.LINE_COMMENT) {
                commentLines++;
            } else if (tokenType == JavaParser.COMMENT) {
                String[] lines = token.getText().split("\r?\n|\r");
                commentLines += lines.length;
            }
        }

        return commentLines;
    }

    @Override
    public double getCommentRatio(ParserRuleContext ctx) {
        int totalLines = getTotalLines(ctx);
        if (totalLines == 0) return 0.0;
        return (double) getCommentLines(ctx) / totalLines;
    }

    @Override
    public int getNumberOfMethods(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> (int) classCtx.classBody().classBodyDeclaration().stream()
                        .filter(bodyDecl -> bodyDecl.memberDeclaration() != null)
                        .filter(bodyDecl -> bodyDecl.memberDeclaration().methodDeclaration() != null)
                        .count(),
                0
        );
    }

    @Override
    public int getNumberOfAttributes(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> (int) classCtx.classBody().classBodyDeclaration().stream()
                        .filter(bodyDecl -> bodyDecl.memberDeclaration() != null)
                        .filter(bodyDecl -> bodyDecl.memberDeclaration().fieldDeclaration() != null)
                        .count(),
                0
        );
    }
}
