package edu.usb.argos.astprocessor.analyzer.core.services;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MethodTooLongAnalyzer<S> implements ICodeSmellNodeAnalyzer<MethodInformation<S>> {

    private final int MAX_METHOD_LENGTH;
    private final IMethodLineAnalyzer<S> methodLineAnalyzer;
    @Getter
    private CodeAnalysisReportHandlerByClass analysisReportHandlerByClass;

    @Override
    public void analyze(MethodInformation<S> method) {
        int methodSize = methodLineAnalyzer.calculateMethodSize(method);

        if (methodSize > MAX_METHOD_LENGTH) {
            int startLine = methodLineAnalyzer.getMethodStartLine(method);
            int endLine = methodLineAnalyzer.getMethodEndLine(method);
            analysisReportHandlerByClass.addMethodWithMethodTooLong(startLine, endLine);
        }
    }
}
