package com.softdevsix.argos.codeSmells.analyzers;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class MethodAnalyzer {
    protected List<MethodDeclaration> getMethods(CompilationUnit cu) {
        return cu.findAll(MethodDeclaration.class);
    }

    protected Set<String> getDeclaredVariables(MethodDeclaration method) {
        Set<String> declaredVariables = new HashSet<>();
        List<VariableDeclarationExpr> variableDeclarations = method.findAll(VariableDeclarationExpr.class);
        for (VariableDeclarationExpr variableDeclaration : variableDeclarations) {
            variableDeclaration.getVariables().forEach(var -> declaredVariables.add(var.getNameAsString()));
        }
        return declaredVariables;
    }

    protected Set<String> getUsedVariables(MethodDeclaration method) {
        Set<String> usedVariables = new HashSet<>();
        List<NameExpr> expressions = method.findAll(NameExpr.class);
        for (NameExpr expr : expressions) {
            usedVariables.add(expr.getNameAsString());
        }
        return usedVariables;
    }
}
