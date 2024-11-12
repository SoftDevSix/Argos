package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.*;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IExpressionCollector;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IMethodAnalyzerVisitor;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IModifierCollector;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IStatementCollector;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class JavaMethodVisitor extends JavaParserBaseVisitor<MethodInfo>
        implements IMethodAnalyzerVisitor<ParserRuleContext, MethodInfo> {

    private final CommonTokenStream tokenStream;

    private final IStatementCollector<JavaParser.StatementContext, JavaParser.MethodDeclarationContext> statementCollector;
    private final IExpressionCollector<JavaParser.ExpressionContext, JavaParser.MethodDeclarationContext> expressionCollector;
    private final IModifierCollector<ParserRuleContext> modifierCollector;

    public JavaMethodVisitor(
            CommonTokenStream tokenStream,
            IStatementCollector<JavaParser.StatementContext, JavaParser.MethodDeclarationContext>  statementCollector,
            IExpressionCollector<JavaParser.ExpressionContext, JavaParser.MethodDeclarationContext> expressionCollector,
            IModifierCollector<ParserRuleContext> modifierCollector) {
        this.tokenStream = tokenStream;
        this.statementCollector = statementCollector;
        this.expressionCollector = expressionCollector;
        this.modifierCollector = modifierCollector;
    }

    @Override
    public MethodInfo visitMethod(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.MethodDeclarationContext.class,
                methodCtx -> buildMethodInfo(methodCtx),
                null
        );
    }

    @Override
    public List<String> getMethodModifiers(ParserRuleContext ctx) {
        return modifierCollector.collectModifiers(ctx);
    }

    @Override
    public String getReturnType(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.MethodDeclarationContext.class,
                methodCtx -> methodCtx.typeTypeOrVoid().getText(),
                ""
        );
    }

    @Override
    public List<ParameterInfo> getParameters(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.MethodDeclarationContext.class,
                methodCtx -> {
                    List<ParameterInfo> parameters = new ArrayList<>();
                    if (methodCtx.formalParameters().formalParameterList() != null) {
                        methodCtx.formalParameters()
                                .formalParameterList()
                                .formalParameter()
                                .forEach(param -> {
                                    String paramName = param.variableDeclaratorId().getText();
                                    String paramType = param.typeType().getText();
                                    parameters.add(new ParameterInfo(paramName, paramType));
                                });
                    }
                    return parameters;
                },
                new ArrayList<>()
        );
    }

    private MethodInfo buildMethodInfo(JavaParser.MethodDeclarationContext methodCtx) {
        return new MethodInfo(
                getName(methodCtx),
                getReturnType(methodCtx),
                getMethodModifiers(methodCtx),
                getParameters(methodCtx),
                statementCollector.collectStatements(methodCtx),
                expressionCollector.collectExpressions(methodCtx),
                tokenStream
        );
    }

    private String getName(JavaParser.MethodDeclarationContext methodCtx) {
        return methodCtx.identifier().getText();
    }

}