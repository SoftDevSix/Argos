package edu.usb.argos.astprocessor.analyzer.core.interfaces.builders;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;

public interface IAnalyzerBuilder<T> {
    ICodeSmellNodeAnalyzer<T> buildAnalyzer(
            CodeSmellsRules codeSmellsRules,
            CodeAnalysisReportHandlerByClass reportHandlerByClass);
}
