package edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IExcessiveParametersAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.services.ExcessiveParametersAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.MethodLineAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;

public class AntlrExcessiveParametersAnalyzerBuilder implements IExcessiveParametersAnalyzerBuilder<JavaParser.StatementContext> {

    @Override
    public ICodeSmellNodeAnalyzer<MethodInformation<JavaParser.StatementContext>> buildAnalyzer(
            CodeSmellsRules codeSmellsRules,
            CodeAnalysisReportHandlerByClass reportHandlerByClass) {
        return ExcessiveParametersAnalyzer.<JavaParser.StatementContext>builder()
                .MAX_PARAMETERS(codeSmellsRules.getMaxParameters())
                .methodLineAnalyzer(buildMethodLineAnalyzer())
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }

    private IMethodLineAnalyzer<JavaParser.StatementContext> buildMethodLineAnalyzer() {
        return new MethodLineAnalyzer();
    }
}
