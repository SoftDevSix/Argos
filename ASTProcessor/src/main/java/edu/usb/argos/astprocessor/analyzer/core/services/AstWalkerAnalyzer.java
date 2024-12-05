package edu.usb.argos.astprocessor.analyzer.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.Getter;

public class AstWalkerAnalyzer {

    @Getter
    private final List<CodeSmellAnalysisByClass> reports;
    private final HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap;

    public AstWalkerAnalyzer(HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap) {
        this.analyzerMap = analyzerMap;
        this.reports = new ArrayList<>();
    }

    public void walkAnalyzers(List<ClassInformation<JavaParser.StatementContext>> classes) {
        for (ClassInformation<JavaParser.StatementContext> classInfo : classes) {
            String classIdentity = buildClassIdentity(classInfo);
            CodeSmellAnalysisByClass classReport = new CodeSmellAnalysisByClass(classIdentity);

            processNode(classInfo, classReport);

            for (MethodInformation<JavaParser.StatementContext> method : classInfo.getMembers().getMethods()) {
                processNode(method, classReport);
            }

            reports.add(classReport);
        }
    }

    private <T> void processNode(T node, CodeSmellAnalysisByClass classReport) {
        List<ICodeSmellNodeAnalyzer<T>> analyzers = getAnalyzersForNode(node);
        for (ICodeSmellNodeAnalyzer<T> analyzer : analyzers) {
            analyzer.getReportHandlerByClass().setCodeSmellAnalysisByClass(classReport);
            analyzer.analyze(node);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<ICodeSmellNodeAnalyzer<T>> getAnalyzersForNode(T node) {
        return analyzerMap.getOrDefault(node.getClass(), List.of())
                .stream()
                .map(analyzer -> (ICodeSmellNodeAnalyzer<T>) analyzer)
                .toList();
    }

    private String buildClassIdentity(ClassInformation<JavaParser.StatementContext> classInfo) {
        StringBuilder classIdentity = new StringBuilder();
        classInfo.getIdentity().getPackageName().ifPresent(packageName -> classIdentity.append(packageName).append("/"));
        classInfo.getIdentity().getName().ifPresent(className -> classIdentity.append(className).append(".java"));

        return classIdentity.toString();
    }
}
