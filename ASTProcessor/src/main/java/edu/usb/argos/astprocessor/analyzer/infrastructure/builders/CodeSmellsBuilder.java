package edu.usb.argos.astprocessor.analyzer.infrastructure.builders;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IExcessiveParametersAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IMethodTooLongAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.INoDuplicateCodeAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CodeSmellsBuilder<S> {

    private final CodeSmellsRules codeSmellsRules;
    private final CodeAnalysisReportHandlerByClass reportHandlerByClass;
    private final IMethodTooLongAnalyzerBuilder<S> methodTooLongAnalyzerBuilder;
    private final IExcessiveParametersAnalyzerBuilder<S> parametersAnalyzerBuilder;
    private final INoDuplicateCodeAnalyzerBuilder<S> noDuplicatedCodeAnalyzerBuilder;
    private final HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzers;

    @Builder
    public CodeSmellsBuilder(CodeSmellsRules codeSmellsRules, CodeAnalysisReportHandlerByClass reportHandlerByClass, IMethodTooLongAnalyzerBuilder<S> methodTooLongAnalyzerBuilder, IExcessiveParametersAnalyzerBuilder<S> parametersAnalyzerBuilder, INoDuplicateCodeAnalyzerBuilder<S> noDuplicatedCodeAnalyzerBuilder) {
        this.reportHandlerByClass = reportHandlerByClass;
        this.analyzers = new HashMap<>();

        this.codeSmellsRules = codeSmellsRules;
        this.methodTooLongAnalyzerBuilder = methodTooLongAnalyzerBuilder;
        this.parametersAnalyzerBuilder = parametersAnalyzerBuilder;
        this.noDuplicatedCodeAnalyzerBuilder = noDuplicatedCodeAnalyzerBuilder;
    }

    public HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> getAnalyzers() {
        buildClassAnalyzer();
        buildMethodAnalyzer();

        return analyzers;
    }

    private void buildMethodAnalyzer() {
        if (codeSmellsRules.isMethodTooLong()) {
            analyzers.computeIfAbsent(MethodInformation.class, k -> new ArrayList<>())
                    .add(methodTooLongAnalyzerBuilder.buildAnalyzer(codeSmellsRules, reportHandlerByClass));
        }
        if (codeSmellsRules.isExcessiveParameters()) {
            analyzers.computeIfAbsent(MethodInformation.class, k -> new ArrayList<>())
                    .add(parametersAnalyzerBuilder.buildAnalyzer(codeSmellsRules, reportHandlerByClass));
        }
    }

    private void buildClassAnalyzer() {
        if (codeSmellsRules.isNoDuplicatedCode()) {
            analyzers.computeIfAbsent(ClassInformation.class, k -> new ArrayList<>())
                    .add(noDuplicatedCodeAnalyzerBuilder.buildAnalyzer(codeSmellsRules, reportHandlerByClass));
        }
    }
}
