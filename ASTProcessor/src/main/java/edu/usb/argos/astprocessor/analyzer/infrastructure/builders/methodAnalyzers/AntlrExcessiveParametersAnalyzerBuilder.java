package edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IMethodAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.services.ExcessiveParametersAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.MethodLineAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AntlrExcessiveParametersAnalyzerBuilder implements IMethodAnalyzerBuilder<JavaParser.StatementContext> {

    private final int maxParameters;
    private final CodeAnalysisReportHandlerByClass reportHandlerByClass;

    @Override
    public ICodeSmellNodeAnalyzer<MethodInformation<JavaParser.StatementContext>> buildAnalyzer() {
        return ExcessiveParametersAnalyzer.<JavaParser.StatementContext>builder()
                .MAX_PARAMETERS(maxParameters)
                .methodLineAnalyzer(buildMethodLineAnalyzer())
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }

    private IMethodLineAnalyzer<JavaParser.StatementContext> buildMethodLineAnalyzer() {
        return new MethodLineAnalyzer();
    }
}
