package edu.usb.argos.astprocessor.analyzer.core.services;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;

import java.util.Optional;

public class MethodTooLongAnalyzer<S> implements ICodeSmellNodeAnalyzer<MethodInformation<S>> {

    private final int MAX_METHOD_LENGTH;
    private final IMethodLineAnalyzer<S> methodLineAnalyzer;
    private Optional<CodeAnalysisReportHandlerByClass> analysisReportHandlerByClass;

    public MethodTooLongAnalyzer(final int maxMethodLength, IMethodLineAnalyzer<S> methodLineAnalyzer) {
        this.MAX_METHOD_LENGTH = maxMethodLength;
        this.methodLineAnalyzer = methodLineAnalyzer;
        this.analysisReportHandlerByClass = Optional.empty();
    }

    @Override
    public void analyze(MethodInformation<S> method) {
        analysisReportHandlerByClass.ifPresent(report -> {
            int methodSize = methodLineAnalyzer.calculateMethodSize(method);
            if (methodSize > MAX_METHOD_LENGTH) {
                int startLine = methodLineAnalyzer.getMethodStartLine(method);
                int endLine = methodLineAnalyzer.getMethodEndLine(method);
                report.addMethodWithMethodTooLong(startLine, endLine);
            }
        });
    }

    @Override
    public void setCodeAnalyzerReport(CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass) {
        this.analysisReportHandlerByClass = Optional.of(codeAnalysisReportHandlerByClass);
    }
}