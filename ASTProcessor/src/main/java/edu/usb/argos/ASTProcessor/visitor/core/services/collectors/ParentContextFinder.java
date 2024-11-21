package edu.usb.argos.ASTProcessor.visitor.core.services.collectors;

import java.util.Optional;

import org.antlr.v4.runtime.ParserRuleContext;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;

public class ParentContextFinder {
    public JavaParser.ClassBodyDeclarationContext findClassBodyDeclarationContext(ParserRuleContext ctx) {
        return Optional.ofNullable(ctx)
                .map(ParserRuleContext::getParent)
                .map(this::resolveParentContext)
                .map(this::convertToClassBodyContext)
                .orElse(null);
    }

    private ParserRuleContext resolveParentContext(ParserRuleContext parent) {
        if (parent instanceof JavaParser.MemberDeclarationContext) {
            return parent.getParent();
        }
        return parent;
    }

    private JavaParser.ClassBodyDeclarationContext convertToClassBodyContext(ParserRuleContext ctx) {
        if (ctx instanceof JavaParser.ClassBodyDeclarationContext classBodyCtx) {
            return classBodyCtx;
        }
        return null;
    }
}
