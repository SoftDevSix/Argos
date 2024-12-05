package edu.usb.argos.astprocessor.analyzer.core.interfaces;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;

public interface ICodeSmellNodeAnalyzer<T> {
    void analyze(T node);
    CodeAnalysisReportHandlerByClass getReportHandlerByClass();
}
