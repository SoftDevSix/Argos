package edu.usb.argos.astprocessor.analyzer.core.services;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
public class MethodTooLongAnalyzer<S> implements ICodeSmellNodeAnalyzer<MethodInformation<S>> {

    private final int maxMethodLength;
    private final IMethodLineAnalyzer<S> methodLineAnalyzer;
    @Getter
    private CodeAnalysisReportHandlerByClass reportHandlerByClass;

    @Override
    public void analyze(MethodInformation<S> method) {
        int methodSize = methodLineAnalyzer.calculateMethodSize(method);

        if (methodSize > maxMethodLength) {
            int startLine = methodLineAnalyzer.getMethodStartLine(method);
            int endLine = methodLineAnalyzer.getMethodEndLine(method);
            reportHandlerByClass.addMethodWithMethodTooLong(startLine, endLine);
        }
    }
}
