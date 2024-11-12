package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.classes.ClassInfo;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes.IClassAnalyzerVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JavaClassVisitor extends JavaParserBaseVisitor<ClassInfo> implements IClassAnalyzerVisitor<ParserRuleContext> {

    @Override
    public ClassInfo visitClassDeclaration(ParserRuleContext ctx) {
        return validateAndExecute(ctx, classCtx -> {
            String name = getClassName(classCtx);
            List<String> modifiers = getClassModifiers(classCtx);
            List<String> interfaces = getImplementedInterfaces(classCtx);
            String superClass = getSuperClass(classCtx);

            return new ClassInfo(name, modifiers, interfaces, superClass);
        }, null);
    }

    @Override
    public String getClassName(ParserRuleContext ctx) {
        return validateAndExecute(ctx, classCtx -> classCtx.identifier().getText(), "");
    }

    @Override
    public List<String> getClassModifiers(ParserRuleContext ctx) {
        return validateAndExecute(ctx, classCtx -> {
            List<String> modifiers = new ArrayList<>();
            if (classCtx.getParent() instanceof JavaParser.ClassBodyDeclarationContext parentCtx) {
                parentCtx.modifier().forEach(modifierContext -> modifiers.add(modifierContext.getText()));
            }
            return modifiers;
        }, new ArrayList<>());
    }

    @Override
    public List<String> getImplementedInterfaces(ParserRuleContext ctx) {
        return validateAndExecute(ctx, classCtx -> {
            List<String> interfaces = new ArrayList<>();
            if (classCtx.typeList() != null) {
                classCtx.typeList().forEach(type -> interfaces.add(type.getText()));
            }
            return interfaces;
        }, new ArrayList<>());
    }

    @Override
    public String getSuperClass(ParserRuleContext ctx) {
        return validateAndExecute(ctx, classCtx -> {
            if (classCtx.typeType() != null) {
                return classCtx.typeType().getText();
            }
            return null;
        }, null);
    }

    private <T> T validateAndExecute(
            ParserRuleContext ctx,
            Function<JavaParser.ClassDeclarationContext, T> operation,
            T defaultValue) {
        if (ctx instanceof JavaParser.ClassDeclarationContext classCtx) {
            return operation.apply(classCtx);
        }
        return defaultValue;
    }
}
