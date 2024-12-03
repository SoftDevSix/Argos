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
    List<CodeSmellAnalysisByClass> reports;
    HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap;

    public AstWalkerAnalyzer(HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap) {
        this.analyzerMap = analyzerMap;
        this.reports = new ArrayList<>();
    }

    public void walkAnalyzers(List<ClassInformation<JavaParser.StatementContext>> classes) {
        for (ClassInformation<JavaParser.StatementContext> classInfo : classes) {
            CodeSmellAnalysisByClass classReport = new CodeSmellAnalysisByClass("ClassPath.java");

            if (analyzerMap.containsKey(classInfo.getClass())) {
                List<ICodeSmellNodeAnalyzer<ClassInformation<JavaParser.StatementContext>>> analyzers = analyzerMap.get(classInfo.getClass())
                        .stream()
                        .map(m -> (ICodeSmellNodeAnalyzer<ClassInformation<JavaParser.StatementContext>>) m)
                        .toList();
                for (ICodeSmellNodeAnalyzer<ClassInformation<JavaParser.StatementContext>> analyzer : analyzers) {
                    analyzer.getReportHandlerByClass().setCodeSmellAnalysisByClass(classReport);
                    analyzer.analyze(classInfo);
                }
            }

            for (MethodInformation<JavaParser.StatementContext> method : classInfo.getMembers().getMethods()) {
                List<ICodeSmellNodeAnalyzer<MethodInformation<JavaParser.StatementContext>>> analyzers = new ArrayList<>();
                if (analyzerMap.containsKey(method.getClass())) {
                    analyzers = analyzerMap.get(method.getClass())
                            .stream()
                            .map(m -> (ICodeSmellNodeAnalyzer<MethodInformation<JavaParser.StatementContext>>) m)
                            .toList();
                }
                for (ICodeSmellNodeAnalyzer<MethodInformation<JavaParser.StatementContext>> analyzer : analyzers) {
                    analyzer.getReportHandlerByClass().setCodeSmellAnalysisByClass(classReport);
                    analyzer.analyze(method);
                }

            }

            reports.add(classReport);
        }
    }
}
