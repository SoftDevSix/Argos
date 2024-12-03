package edu.usb.argos.astprocessor.analyzer.core.services;

import java.util.List;
import java.util.Optional;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.ExcessiveParametersMethodAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import lombok.Getter;
import lombok.AllArgsConstructor;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

@AllArgsConstructor
public class ExcessiveParametersAnalyzer<K> implements ICodeSmellNodeAnalyzer<MethodInformation<K>> {
    private final int MAX_PARAMETERS;
    private final IMethodLineAnalyzer<K> methodLineAnalyzer;
    @Getter
    private CodeAnalysisReportHandlerByClass analysisReportHandlerByClass;

    @Override
    public void analyze(MethodInformation<K> method) {
        int parameterCount = method.getParameters().size();
        if (parameterCount > MAX_PARAMETERS) {
            int startLine = methodLineAnalyzer.getMethodStartLine(method);
            int endLine = methodLineAnalyzer.getMethodEndLine(method);
            analysisReportHandlerByClass.addMethodWithExcessiveParameters(startLine, endLine);
        }
    }
}
