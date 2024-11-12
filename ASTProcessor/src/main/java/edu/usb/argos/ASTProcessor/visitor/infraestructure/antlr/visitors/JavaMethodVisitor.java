package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.*;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IMethodAnalyzerVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JavaMethodVisitor extends JavaParserBaseVisitor<MethodInfo>
        implements IMethodAnalyzerVisitor<ParserRuleContext> {

    private final CommonTokenStream tokenStream;

    public JavaMethodVisitor(CommonTokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    @Override
    public MethodInfo visitMethod(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
            String name = methodCtx.identifier().getText();
            String returnType = getReturnType(methodCtx);
            List<String> modifiers = getMethodModifiers(methodCtx);
            List<ParameterInfo> parameters = getParameters(methodCtx);
            List<JavaParser.StatementContext> statements = collectStatements(methodCtx);
            List<JavaParser.ExpressionContext> expressions = collectExpressions(methodCtx);

            return new MethodInfo(
                    name,
                    returnType,
                    modifiers,
                    parameters,
                    statements,
                    expressions,
                    tokenStream
            );
        }, null);
    }

    @Override
    public List<String> getMethodModifiers(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
            List<String> modifiers = new ArrayList<>();

            ParserRuleContext parent = methodCtx.getParent();
            if (parent instanceof JavaParser.MemberDeclarationContext) {
                parent = parent.getParent();
            }

            if (parent instanceof JavaParser.ClassBodyDeclarationContext parentCtx) {
                List<JavaParser.ModifierContext> modifierContexts = parentCtx.modifier();
                if (modifierContexts != null) {
                    for (JavaParser.ModifierContext mod : modifierContexts) {
                        if (mod.classOrInterfaceModifier() != null) {
                            if (mod.classOrInterfaceModifier().PUBLIC() != null) {
                                modifiers.add("public");
                            } else if (mod.classOrInterfaceModifier().PRIVATE() != null) {
                                modifiers.add("private");
                            } else if (mod.classOrInterfaceModifier().PROTECTED() != null) {
                                modifiers.add("protected");
                            } else if (mod.classOrInterfaceModifier().STATIC() != null) {
                                modifiers.add("static");
                            } else if (mod.classOrInterfaceModifier().FINAL() != null) {
                                modifiers.add("final");
                            }
                        }
                    }
                }
            }
            return modifiers;
        }, new ArrayList<>());
    }

    @Override
    public String getReturnType(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx ->
                methodCtx.typeTypeOrVoid().getText(), ""
        );
    }

    @Override
    public List<ParameterInfo> getParameters(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
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
        }, new ArrayList<>());
    }

    private List<JavaParser.StatementContext> collectStatements(JavaParser.MethodDeclarationContext ctx) {
        List<JavaParser.StatementContext> statements = new ArrayList<>();

        if (ctx.methodBody() != null && ctx.methodBody().block() != null) {
            JavaParser.BlockContext block = ctx.methodBody().block();
            collectStatementsRecursive(block, statements);
        }
        return statements;
    }

    private void collectStatementsRecursive(JavaParser.BlockContext block,
                                            List<JavaParser.StatementContext> statements) {
        if (block == null || block.blockStatement() == null) return;

        for (JavaParser.BlockStatementContext blockStatement : block.blockStatement()) {
            if (blockStatement.statement() != null) {
                if (blockStatement.statement().blockLabel != null) {
                    collectStatementsRecursive(blockStatement.statement().blockLabel, statements);
                } else {
                    statements.add(blockStatement.statement());

                    if (blockStatement.statement().block() != null) {
                        collectStatementsRecursive(blockStatement.statement().block(), statements);
                    }
                }
            }
        }
    }


    private static class ExpressionCollectorVisitor extends JavaParserBaseVisitor<Void> {
        private final List<JavaParser.ExpressionContext> expressions = new ArrayList<>();

        @Override
        public Void visitExpression(JavaParser.ExpressionContext ctx) {
            expressions.add(ctx);
            visitChildren(ctx);
            return null;
        }

        public List<JavaParser.ExpressionContext> getExpressions() {
            return expressions;
        }
    }

    private List<JavaParser.ExpressionContext> collectExpressions(JavaParser.MethodDeclarationContext methodCtx) {
        ExpressionCollectorVisitor expressionCollector = new ExpressionCollectorVisitor();
        methodCtx.accept(expressionCollector);
        return expressionCollector.getExpressions();
    }

    private <T> T validateAndExecute(
            ParserRuleContext ctx,
            Function<JavaParser.MethodDeclarationContext, T> operation,
            T defaultValue) {
        if (ctx instanceof JavaParser.MethodDeclarationContext methodCtx) {
            return operation.apply(methodCtx);
        }
        return defaultValue;
    }
}