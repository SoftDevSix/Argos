package edu.usb.argos.ASTProcessor.analyzer.core.services;

import java.util.List;
import java.util.Optional;

import edu.usb.argos.ASTProcessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.ASTProcessor.analyzer.infrastructure.utils.ExcessiveParametersMethodAnalyzer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces.IMethodLineAnalyzer;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class ExcessiveParametersAnalyzer<K> implements ICodeSmellNodeAnalyzer<MethodInformation<K>> {
    private final int MAX_PARAMETERS;
    private final IMethodLineAnalyzer<K> methodLineAnalyzer;
    private Optional<CodeAnalysisReportHandlerByClass> analysisReportHandlerByClass;

    public ExcessiveParametersAnalyzer(final int maxParameters, IMethodLineAnalyzer<K> methodLineAnalyzer ) {
        this.MAX_PARAMETERS = maxParameters;
        this.methodLineAnalyzer = methodLineAnalyzer;
        this.analysisReportHandlerByClass = Optional.empty();
    }

    @Override
    public void analyze(MethodInformation<K> method) {
        analysisReportHandlerByClass.ifPresent(report -> {
            int parameterCount = method.getParameters().size();
            if (parameterCount > MAX_PARAMETERS) {
                int startLine = methodLineAnalyzer.getMethodStartLine(method);
                int endLine = methodLineAnalyzer.getMethodEndLine(method);

                report.addMethodWithExcessiveParameters(startLine, endLine);
            }
        });
    }

    @Override
    public void setCodeAnalyzerReport(CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass) {
        this.analysisReportHandlerByClass = Optional.of(codeAnalysisReportHandlerByClass);
    }
}