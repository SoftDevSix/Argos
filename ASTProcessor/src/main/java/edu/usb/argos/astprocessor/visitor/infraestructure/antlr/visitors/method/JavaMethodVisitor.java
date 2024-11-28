package edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.IAnnotationExtractor;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.IModifierExtractor;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.IParameterExtractor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Value
public class JavaMethodVisitor extends JavaParserBaseVisitor<MethodInformation<JavaParser.StatementContext>> {
    IModifierExtractor<JavaParser.ClassBodyDeclarationContext> modifierService;
    IParameterExtractor<JavaParser.FormalParameterContext, JavaParser.LastFormalParameterContext> parameterService;
    IAnnotationExtractor<JavaParser.ClassBodyDeclarationContext> annotationService;

    @Override
    public MethodInformation<JavaParser.StatementContext> visitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        return MethodInformation.<JavaParser.StatementContext>builder()
                .name(getName(ctx).get())
                .returnType(getReturnType(ctx).get())
                .modifiers(getMethodModifiers(ctx).orElse(Collections.emptyList()))
                .parameters(getParameters(ctx))
                .statements(getStatements(ctx))
                .throwsExceptions(getThrowsExceptions(ctx))
                .annotations(getAnnotations(ctx).orElse(Collections.emptyList()))
                .isVarArgs(hasVarArgs(ctx))
                .build();
    }

    private Optional<String> getName(JavaParser.MethodDeclarationContext ctx) {
        if (ctx == null || ctx.identifier() == null) {
            return Optional.empty();
        }
        return Optional.of(ctx.identifier().getText());
    }

    private boolean hasVarArgs(JavaParser.MethodDeclarationContext ctx) {
        if (ctx.formalParameters() == null) {
            return false;
        }

        JavaParser.FormalParameterListContext paramList = ctx.formalParameters().formalParameterList();
        if (paramList == null) {
            return false;
        }

        return paramList.lastFormalParameter() != null;
    }

    private Optional<String> getReturnType(JavaParser.MethodDeclarationContext ctx) {
        return Optional.ofNullable(ctx)
                .map(JavaParser.MethodDeclarationContext::typeTypeOrVoid)
                .map(JavaParser.TypeTypeOrVoidContext::getText);
    }

    private Optional<List<String>> getMethodModifiers(JavaParser.MethodDeclarationContext ctx) {
        return Optional.ofNullable(ctx)
                .map(this::findClassBodyDeclarationContext)
                .flatMap(modifierService::extractModifiers);
    }

    private List<ParameterInformation> getParameters(JavaParser.MethodDeclarationContext ctx) {
        return Optional.ofNullable(ctx)
                .map(JavaParser.MethodDeclarationContext::formalParameters)
                .map(JavaParser.FormalParametersContext::formalParameterList)
                .map(this::processParameterList)
                .orElse(Collections.emptyList());
    }

    private List<ParameterInformation> processParameterList(JavaParser.FormalParameterListContext paramList) {
        List<ParameterInformation> parameters = new ArrayList<>();
        addRegularParameters(paramList, parameters);
        addVarArgsParameter(paramList, parameters);
        return Collections.unmodifiableList(parameters);
    }

    private void addRegularParameters(JavaParser.FormalParameterListContext paramList, List<ParameterInformation> parameters) {
        if (paramList.formalParameter() != null) {
            for (JavaParser.FormalParameterContext param : paramList.formalParameter()) {
                parameterService.createRegularParameter(param)
                        .ifPresent(parameters::add);
            }
        }
    }

    private void addVarArgsParameter(JavaParser.FormalParameterListContext paramList, List<ParameterInformation> parameters) {
        if (paramList.lastFormalParameter() != null) {
            parameterService.createVarArgsParameter(paramList.lastFormalParameter())
                    .ifPresent(parameters::add);
        }
    }

    private List<JavaParser.StatementContext> getStatements(JavaParser.MethodDeclarationContext ctx) {
        return Optional.ofNullable(ctx)
                .map(JavaParser.MethodDeclarationContext::methodBody)
                .map(JavaParser.MethodBodyContext::block)
                .map(this::processBlockStatements)
                .orElse(Collections.emptyList());
    }

    private List<JavaParser.StatementContext> processBlockStatements(JavaParser.BlockContext block) {
        List<JavaParser.StatementContext> statements = new ArrayList<>();
        for (JavaParser.BlockStatementContext blockStmt : block.blockStatement()) {
            addStatement(blockStmt, statements);
        }
        return Collections.unmodifiableList(statements);
    }

    private void addStatement(JavaParser.BlockStatementContext blockStmt, List<JavaParser.StatementContext> statements) {
        if (blockStmt.statement() != null) {
            statements.add(blockStmt.statement());
        } else if (blockStmt.localVariableDeclaration() != null) {
            statements.add(createLocalVariableStatement(blockStmt));
        }
    }

    private JavaParser.StatementContext createLocalVariableStatement(JavaParser.BlockStatementContext blockStmt) {
        JavaParser.StatementContext statementCtx = new JavaParser.StatementContext(blockStmt, 0);
        statementCtx.statementExpression = blockStmt.localVariableDeclaration().getParent()
                .getRuleContext(JavaParser.ExpressionContext.class, 0);
        return statementCtx;
    }

    private List<String> getThrowsExceptions(JavaParser.MethodDeclarationContext ctx) {
        return Optional.ofNullable(ctx)
                .map(JavaParser.MethodDeclarationContext::qualifiedNameList)
                .map(this::processQualifiedNames)
                .orElse(Collections.emptyList());
    }

    private List<String> processQualifiedNames(JavaParser.QualifiedNameListContext qualifiedList) {
        List<String> exceptions = new ArrayList<>();
        for (JavaParser.QualifiedNameContext name : qualifiedList.qualifiedName()) {
            exceptions.add(name.getText());
        }
        return Collections.unmodifiableList(exceptions);
    }

    private Optional<List<String>> getAnnotations(JavaParser.MethodDeclarationContext ctx) {
        return Optional.ofNullable(ctx)
                .map(this::findClassBodyDeclarationContext)
                .flatMap(annotationService::extractAnnotation);
    }

    private JavaParser.ClassBodyDeclarationContext findClassBodyDeclarationContext(JavaParser.MethodDeclarationContext ctx) {
        ParseTree current = ctx;
        while (current != null && !(current instanceof JavaParser.ClassBodyDeclarationContext)) {
            current = current.getParent();
        }
        return (JavaParser.ClassBodyDeclarationContext) current;
    }

}
