package edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IMethodAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.services.MethodTooLongAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AntlrMethodTooLongAnalyzerBuilder implements IMethodAnalyzerBuilder<JavaParser.StatementContext> {

    private final int maxMethodLength;
    private final CodeAnalysisReportHandlerByClass reportHandlerByClass;
    private final IMethodLineAnalyzer<JavaParser.StatementContext> methodLineAnalyzer;

    @Override
    public ICodeSmellNodeAnalyzer<MethodInformation<JavaParser.StatementContext>> buildAnalyzer() {
        return MethodTooLongAnalyzer.<JavaParser.StatementContext>builder()
                .maxMethodLength(maxMethodLength)
                .methodLineAnalyzer(methodLineAnalyzer)
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }
}
