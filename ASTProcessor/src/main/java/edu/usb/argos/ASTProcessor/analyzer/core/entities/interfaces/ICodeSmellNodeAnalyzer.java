package edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces;

import edu.usb.argos.ASTProcessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;

public interface ICodeSmellNodeAnalyzer<T> {
    void analyze(T node);
    void setCodeAnalyzerReport(CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass);
}
