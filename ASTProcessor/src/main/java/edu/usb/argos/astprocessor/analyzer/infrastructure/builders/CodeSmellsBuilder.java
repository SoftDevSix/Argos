package edu.usb.argos.astprocessor.analyzer.infrastructure.builders;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.classAnalyzers.LshNoDuplicatedCodeAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers.AntlrExcessiveParametersAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers.AntlrMethodTooLongAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CodeSmellsBuilder {

    private final CodeSmellsRules codeSmellsRules;
    private final AntlrMethodTooLongAnalyzerBuilder methodTooLongAnalyzerBuilder;
    private final LshNoDuplicatedCodeAnalyzerBuilder noDuplicatedCodeAnalyzerBuilder;
    private final AntlrExcessiveParametersAnalyzerBuilder parametersAnalyzerBuilder;
    private final HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzers;

    @Builder
    public CodeSmellsBuilder(CodeSmellsRules codeSmellsRules, AntlrMethodTooLongAnalyzerBuilder methodTooLongAnalyzerBuilder, LshNoDuplicatedCodeAnalyzerBuilder noDuplicatedCodeAnalyzerBuilder, AntlrExcessiveParametersAnalyzerBuilder parametersAnalyzerBuilder) {
        this.analyzers = new HashMap<>();

        this.codeSmellsRules = codeSmellsRules;
        this.parametersAnalyzerBuilder = parametersAnalyzerBuilder;
        this.methodTooLongAnalyzerBuilder = methodTooLongAnalyzerBuilder;
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
                    .add(methodTooLongAnalyzerBuilder.buildAnalyzer());
        }
        if (codeSmellsRules.isExcessiveParameters()) {
            analyzers.computeIfAbsent(MethodInformation.class, k -> new ArrayList<>())
                    .add(parametersAnalyzerBuilder.buildAnalyzer());
        }
    }

    private void buildClassAnalyzer() {
        if (codeSmellsRules.isNoDuplicatedCode()) {
            analyzers.computeIfAbsent(ClassInformation.class, k -> new ArrayList<>())
                    .add(noDuplicatedCodeAnalyzerBuilder.buildAnalyzer());
        }
    }
}
