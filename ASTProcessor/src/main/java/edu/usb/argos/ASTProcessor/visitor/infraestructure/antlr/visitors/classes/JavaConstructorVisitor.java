package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes;

import java.util.ArrayList;
import java.util.List;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor.IConstructorAnalyzer;

public class JavaConstructorVisitor extends JavaParserBaseVisitor<List<ConstructorInformation<JavaParser.BlockStatementContext>>> implements IConstructorAnalyzer<JavaParser.ClassBodyContext, JavaParser.BlockStatementContext> {

    @Override
    public List<ConstructorInformation<JavaParser.BlockStatementContext>> visitConstructors(JavaParser.ClassBodyContext classContext) {
        List<ConstructorInformation<JavaParser.BlockStatementContext>> constructors = new ArrayList<>();

        for (JavaParser.ClassBodyDeclarationContext bodyCtx : classContext.classBodyDeclaration()) {
            JavaParser.MemberDeclarationContext memberCtx = bodyCtx.memberDeclaration();

            if (memberCtx != null && memberCtx.constructorDeclaration() != null) {
                JavaParser.ConstructorDeclarationContext constructorCtx = memberCtx.constructorDeclaration();
                String constructorName = constructorCtx.identifier().getText();
                List<String> modifiers = getModifiers(bodyCtx);
                List<String> parameters = getParameters(constructorCtx);
                List<JavaParser.BlockStatementContext> bodyStatements = getBodyStatements(constructorCtx);
                ConstructorInformation<JavaParser.BlockStatementContext> constructorInformation =
                        ConstructorInformation.<JavaParser.BlockStatementContext>builder()
                                .name(constructorName)
                                .modifiers(modifiers)
                                .parameters(parameters)
                                .bodyStatements(bodyStatements)
                                .build();
                constructors.add(constructorInformation);
            }
        }

        return constructors;
    }

    private List<String> getModifiers(JavaParser.ClassBodyDeclarationContext classContext) {
        List<String> modifiers = new ArrayList<>();

        if (classContext.modifier() != null) {
            for (JavaParser.ModifierContext modCtx : classContext.modifier()) {
                modifiers.add(modCtx.getText());
            }
        }

        return modifiers;
    }

    private List<String> getParameters(JavaParser.ConstructorDeclarationContext constructorContext) {
        List<String> parameters = new ArrayList<>();

        if (constructorContext.formalParameters().formalParameterList() != null) {
            for (JavaParser.FormalParameterContext paramCtx : constructorContext.formalParameters().formalParameterList().formalParameter()) {
                parameters.add(paramCtx.typeType().getText() + " " + paramCtx.variableDeclaratorId().getText());
            }
        }

        return parameters;
    }

    private List<JavaParser.BlockStatementContext> getBodyStatements(JavaParser.ConstructorDeclarationContext constructorContext) {
        List<JavaParser.BlockStatementContext> bodyStatements = new ArrayList<>();

        if (constructorContext.block() != null) {
            bodyStatements.addAll(constructorContext.block().blockStatement());
        }

        return bodyStatements;
    }
}
