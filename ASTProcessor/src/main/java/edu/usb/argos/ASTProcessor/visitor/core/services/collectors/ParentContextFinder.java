package edu.usb.argos.ASTProcessor.visitor.core.services.collectors;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import org.antlr.v4.runtime.ParserRuleContext;

public class ParentContextFinder {
    public JavaParser.ClassBodyDeclarationContext findClassBodyDeclarationContext(ParserRuleContext ctx) {
        ParserRuleContext parent = ctx.getParent();

        if (parent instanceof JavaParser.MemberDeclarationContext) {
            parent = parent.getParent();
        }

        if (parent instanceof JavaParser.ClassBodyDeclarationContext classBodyCtx) {
            return classBodyCtx;
        }

        return null;
    }
}
