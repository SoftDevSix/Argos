package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.*;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IExpressionCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Expression;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Token;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor.IMethodAnalyzerVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IModifierCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IStatementCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrExpressionAdapter;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrStatementAdapter;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrTokenAdapter;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaMethodVisitor extends
        JavaParserBaseVisitor<MethodInfo<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream>>
        implements IMethodAnalyzerVisitor<ParserRuleContext,
        MethodInfo<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream>> {

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
    public MethodInfo<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream> visitMethod(ParserRuleContext ctx) {
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

    private MethodInfo<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream>
    buildMethodInfo(JavaParser.MethodDeclarationContext methodCtx) {
        List<Statement<JavaParser.StatementContext>> statements =
                statementCollector.collectStatements(methodCtx).stream()
                        .map(AntlrStatementAdapter::new)
                        .collect(Collectors.toList());

        List<Expression<JavaParser.ExpressionContext>> expressions =
                expressionCollector.collectExpressions(methodCtx).stream()
                        .map(AntlrExpressionAdapter::new)
                        .collect(Collectors.toList());

        Token<CommonTokenStream> tokenAdapter = new AntlrTokenAdapter(tokenStream);

        return new MethodInfo<>(
                getName(methodCtx),
                getReturnType(methodCtx),
                getMethodModifiers(methodCtx),
                getParameters(methodCtx),
                statements,
                expressions,
                tokenAdapter
        );
    }

    private String getName(JavaParser.MethodDeclarationContext methodCtx) {
        return methodCtx.identifier().getText();
    }

}