package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes;

import java.util.ArrayList;
import java.util.List;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor.IConstructorAnalyzer;

public class JavaConstructorVisitor extends JavaParserBaseVisitor<List<ConstructorInformation<JavaParser.StatementContext>>>
        implements IConstructorAnalyzer<JavaParser.ClassBodyContext, JavaParser.StatementContext> {

    @Override
    public List<ConstructorInformation<JavaParser.StatementContext>> visitConstructors(JavaParser.ClassBodyContext classContext) {
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = new ArrayList<>();

        for (JavaParser.ClassBodyDeclarationContext bodyCtx : classContext.classBodyDeclaration()) {
            JavaParser.MemberDeclarationContext memberCtx = bodyCtx.memberDeclaration();

            if (isConstructorDeclaration(memberCtx)) {
                constructors.add(createConstructorInformation(memberCtx, bodyCtx));
            }
        }

        return constructors;
    }

    private boolean isConstructorDeclaration(JavaParser.MemberDeclarationContext memberCtx) {
        return memberCtx != null && memberCtx.constructorDeclaration() != null;
    }

    private ConstructorInformation<JavaParser.StatementContext> createConstructorInformation(
            JavaParser.MemberDeclarationContext memberCtx, JavaParser.ClassBodyDeclarationContext bodyCtx) {
        JavaParser.ConstructorDeclarationContext constructorCtx = memberCtx.constructorDeclaration();
        String constructorName = constructorCtx.identifier().getText();
        List<String> modifiers = getModifiers(bodyCtx);
        List<String> parameters = getParameters(constructorCtx);
        List<JavaParser.StatementContext> bodyStatements = getBodyStatements(constructorCtx);

        return ConstructorInformation.<JavaParser.StatementContext>builder()
                .name(constructorName)
                .modifiers(modifiers)
                .parameters(parameters)
                .bodyStatements(bodyStatements)
                .build();
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

    private List<JavaParser.StatementContext> getBodyStatements(JavaParser.ConstructorDeclarationContext constructorContext) {
        List<JavaParser.StatementContext> statements = new ArrayList<>();

        if (constructorContext.block() != null) {
            for (JavaParser.BlockStatementContext blockStmt : constructorContext.block().blockStatement()) {
                if (blockStmt.statement() != null) {
                    statements.add(blockStmt.statement());
                }
            }
        }

        return statements;
    }
}
