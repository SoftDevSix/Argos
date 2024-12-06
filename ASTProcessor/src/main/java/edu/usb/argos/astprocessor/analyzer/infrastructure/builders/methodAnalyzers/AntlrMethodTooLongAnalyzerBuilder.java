package edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IMethodTooLongAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.services.MethodTooLongAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.MethodLineAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AntlrMethodTooLongAnalyzerBuilder implements IMethodTooLongAnalyzerBuilder<JavaParser.StatementContext> {

    @Override
    public ICodeSmellNodeAnalyzer<MethodInformation<JavaParser.StatementContext>> buildAnalyzer(CodeSmellsRules codeSmellsRules, CodeAnalysisReportHandlerByClass reportHandlerByClass) {
        return MethodTooLongAnalyzer.<JavaParser.StatementContext>builder()
                .maxMethodLength(codeSmellsRules.getMaxMethodLength())
                .methodLineAnalyzer(buildMethodLineAnalyzer())
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }

    private IMethodLineAnalyzer<JavaParser.StatementContext> buildMethodLineAnalyzer() {
        return new MethodLineAnalyzer();
    }
}
