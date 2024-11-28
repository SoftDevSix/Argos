package edu.usb.argos.astprocessor.analyzer.core.entities.handlers;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeAnalysisReport;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeAnalysisReportType;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;

public record CodeAnalysisReportHandlerByClass(CodeSmellAnalysisByClass codeSmellAnalysisByClass) {

    public void addMethodWithExcessiveParameters(int startLine, int endLine) {
        CodeAnalysisReport codeAnalysisReport = CodeAnalysisReport.builder()
                .startLine(startLine)
                .endLine(endLine)
                .message("The method has too many parameters, making it difficult to understand and use.")
                .type(CodeAnalysisReportType.EXCESSIVE_PARAMETERS)
                .build();

        codeSmellAnalysisByClass.addCodeAnalysisReport(codeAnalysisReport);
    }

    public void addMethodWithMagicNumbers(int startLine, int endLine) {
        CodeAnalysisReport codeAnalysisReport = CodeAnalysisReport.builder()
                .startLine(startLine)
                .endLine(endLine)
                .message("Magic numbers were detected in the method, consider using constants with descriptive names.")
                .type(CodeAnalysisReportType.MAGIC_NUMBER)
                .build();

        codeSmellAnalysisByClass.addCodeAnalysisReport(codeAnalysisReport);
    }

    public void addMethodWithMethodTooLong(int startLine, int endLine) {
        CodeAnalysisReport codeAnalysisReport = CodeAnalysisReport.builder()
                .startLine(startLine)
                .endLine(endLine)
                .message("The method is too long, consider breaking it down into smaller, reusable methods.")
                .type(CodeAnalysisReportType.METHOD_TOO_LONG)
                .build();

        codeSmellAnalysisByClass.addCodeAnalysisReport(codeAnalysisReport);
    }

    public void addMethodWithNoDuplicatedCode(int startLine, int endLine) {
        CodeAnalysisReport codeAnalysisReport = CodeAnalysisReport.builder()
                .startLine(startLine)
                .endLine(endLine)
                .message("Duplicate code was detected in the method, consider abstracting it or reusing functions.")
                .type(CodeAnalysisReportType.DUPLICATED_CODE)
                .build();

        codeSmellAnalysisByClass.addCodeAnalysisReport(codeAnalysisReport);
    }
}
