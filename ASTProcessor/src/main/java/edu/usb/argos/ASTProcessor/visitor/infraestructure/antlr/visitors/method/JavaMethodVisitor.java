package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IExpressionCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IModifierCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IStatementCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Expression;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Token;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor.IMethodAnalyzerVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrExpressionAdapter;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrStatementAdapter;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrTokenAdapter;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Value
public class JavaMethodVisitor extends
        JavaParserBaseVisitor<MethodInformation<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream>>
        implements IMethodAnalyzerVisitor<ParserRuleContext,
        MethodInformation<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream>> {

    CommonTokenStream tokenStream;
    IStatementCollector<JavaParser.StatementContext, JavaParser.MethodDeclarationContext> statementCollector;
    IExpressionCollector<JavaParser.ExpressionContext, JavaParser.MethodDeclarationContext> expressionCollector;
    IModifierCollector<ParserRuleContext> modifierCollector;
    private static final String EMPTY_RETURN_TYPE = "";

    @Override
    public MethodInformation<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream> visitMethod(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.MethodDeclarationContext.class,
                this::buildMethodInfo,
                null
        );
    }

    @Override
    public List<String> getMethodModifiers(ParserRuleContext ctx) {
        return modifierCollector.collectModifiers(ctx);
    }

    @Override
    public String getReturnType(ParserRuleContext ctx) {
        return Optional.ofNullable(ctx)
                .map(context -> ContextValidator.validateAndExecute(
                        context,
                        JavaParser.MethodDeclarationContext.class,
                        this::extractReturnType,
                        EMPTY_RETURN_TYPE
                ))
                .orElse(EMPTY_RETURN_TYPE);
    }

    private String extractReturnType(JavaParser.MethodDeclarationContext methodCtx) {
        return methodCtx.typeTypeOrVoid().getText();
    }

    @Override
    public List<ParameterInformation> getParameters(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.MethodDeclarationContext.class,
                this::extractParameters,
                Collections.emptyList()
        );
    }

    private List<ParameterInformation> extractParameters(JavaParser.MethodDeclarationContext methodCtx) {
        return Optional.ofNullable(methodCtx.formalParameters().formalParameterList())
                .map(this::processFormalParameters)
                .orElse(Collections.emptyList());
    }

    private List<ParameterInformation> processFormalParameters(JavaParser.FormalParameterListContext parameterList) {
        return parameterList.formalParameter().stream()
                .map(this::createParameterInfo)
                .collect(Collectors.toList());
    }

    private ParameterInformation createParameterInfo(JavaParser.FormalParameterContext param) {
        return ParameterInformation.builder()
                .name(param.variableDeclaratorId().getText())
                .type(param.typeType().getText())
                .build();
    }

    private MethodInformation<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream>
    buildMethodInfo(JavaParser.MethodDeclarationContext methodCtx) {
        return MethodInformation.<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream>builder()
                .name(extractMethodName(methodCtx))
                .returnType(getReturnType(methodCtx))
                .modifiers(getMethodModifiers(methodCtx))
                .parameters(getParameters(methodCtx))
                .statements(collectMethodStatements(methodCtx))
                .expressions(collectMethodExpressions(methodCtx))
                .tokens(createTokenAdapter())
                .build();
    }

    private String extractMethodName(JavaParser.MethodDeclarationContext methodCtx) {
        return methodCtx.identifier().getText();
    }

    private List<Statement<JavaParser.StatementContext>> collectMethodStatements(JavaParser.MethodDeclarationContext methodCtx) {
        return statementCollector.collectStatements(methodCtx).stream()
                .map(AntlrStatementAdapter::new)
                .collect(Collectors.toList());
    }

    private List<Expression<JavaParser.ExpressionContext>> collectMethodExpressions(JavaParser.MethodDeclarationContext methodCtx) {
        return expressionCollector.collectExpressions(methodCtx).stream()
                .map(AntlrExpressionAdapter::new)
                .collect(Collectors.toList());
    }

    private Token<CommonTokenStream> createTokenAdapter() {
        return new AntlrTokenAdapter(tokenStream);
    }
}