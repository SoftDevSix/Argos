package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes;

import java.util.ArrayList;
import java.util.List;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInfo;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor.IConstructorAnalyzer;

public class JavaConstructorVisitor extends JavaParserBaseVisitor<List<ConstructorInfo<JavaParser.BlockStatementContext>>> implements IConstructorAnalyzer<JavaParser.ClassBodyContext, JavaParser.BlockStatementContext> {

    @Override
    public List<ConstructorInfo<JavaParser.BlockStatementContext>> visitConstructors(JavaParser.ClassBodyContext ctx) {
        List<ConstructorInfo<JavaParser.BlockStatementContext>> constructors = new ArrayList<>();

        for (JavaParser.ClassBodyDeclarationContext bodyCtx : ctx.classBodyDeclaration()) {
            JavaParser.MemberDeclarationContext memberCtx = bodyCtx.memberDeclaration();

            if (memberCtx != null && memberCtx.constructorDeclaration() != null) {
                JavaParser.ConstructorDeclarationContext constructorCtx = memberCtx.constructorDeclaration();
                String constructorName = constructorCtx.identifier().getText();
                List<String> modifiers = getModifiers(bodyCtx);
                List<String> parameters = getParameters(constructorCtx);
                List<JavaParser.BlockStatementContext> bodyStatements = getBodyStatements(constructorCtx);

                constructors.add(new ConstructorInfo<>(constructorName, modifiers, parameters, bodyStatements));
            }
        }

        return constructors;
    }

    private List<String> getModifiers(JavaParser.ClassBodyDeclarationContext ctx) {
        List<String> modifiers = new ArrayList<>();
        if (ctx.modifier() != null) {
            for (JavaParser.ModifierContext modCtx : ctx.modifier()) {
                modifiers.add(modCtx.getText());
            }
        }
        return modifiers;
    }

    private List<String> getParameters(JavaParser.ConstructorDeclarationContext ctx) {
        List<String> parameters = new ArrayList<>();
        if (ctx.formalParameters().formalParameterList() != null) {
            for (JavaParser.FormalParameterContext paramCtx : ctx.formalParameters().formalParameterList().formalParameter()) {
                parameters.add(paramCtx.typeType().getText() + " " + paramCtx.variableDeclaratorId().getText());
            }
        }
        return parameters;
    }

    private List<JavaParser.BlockStatementContext> getBodyStatements(JavaParser.ConstructorDeclarationContext ctx) {
        List<JavaParser.BlockStatementContext> bodyStatements = new ArrayList<>();
        if (ctx.block() != null) {
            bodyStatements.addAll(ctx.block().blockStatement());
        }
        return bodyStatements;
    }
}
