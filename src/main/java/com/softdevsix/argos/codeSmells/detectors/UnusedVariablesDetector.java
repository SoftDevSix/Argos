package com.softdevsix.argos.codeSmells.detectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.softdevsix.argos.codeSmells.analyzers.MethodAnalyzer;
import com.softdevsix.argos.codeSmells.interfaces.ICodeSmellDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UnusedVariablesDetector extends MethodAnalyzer implements ICodeSmellDetector {

    @Override
    public List<String> detect(CompilationUnit cu) {
        List<String> unusedVariables = new ArrayList<>();
        List<MethodDeclaration> methods = getMethods(cu);

        for (MethodDeclaration method : methods) {
            Set<String> declaredVariables = getDeclaredVariables(method);
            Set<String> usedVariables = getUsedVariables(method);

            for (String variable : declaredVariables) {
                if (!usedVariables.contains(variable)) {
                    unusedVariables.add(variable);
                }
            }
        }
        return unusedVariables;
    }
}
