package edu.usb.argos.astprocessor.analyzer.core.services;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Builder
@AllArgsConstructor
public class ExcessiveParametersAnalyzer<K> implements ICodeSmellNodeAnalyzer<MethodInformation<K>> {
    private final int MAX_PARAMETERS;
    private final IMethodLineAnalyzer<K> methodLineAnalyzer;
    @Getter
    private CodeAnalysisReportHandlerByClass reportHandlerByClass;

    @Override
    public void analyze(MethodInformation<K> method) {
        int parameterCount = method.getParameters().size();
        if (parameterCount > MAX_PARAMETERS) {
            int startLine = methodLineAnalyzer.getMethodStartLine(method);
            int endLine = methodLineAnalyzer.getMethodEndLine(method);
            reportHandlerByClass.addMethodWithExcessiveParameters(startLine, endLine);
        }
    }
}
