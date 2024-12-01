package edu.usb.argos.astprocessor.analyzer.core.entities.handlers;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeAnalysisReport;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeAnalysisReportType;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CodeAnalysisReportHandlerByClass {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<CodeSmellAnalysisByClass> codeSmellAnalysisByClass;

    public CodeAnalysisReportHandlerByClass(CodeSmellAnalysisByClass codeSmellAnalysisByClass) {
        this.codeSmellAnalysisByClass = Optional.of(codeSmellAnalysisByClass);
    }

    public CodeAnalysisReportHandlerByClass() {
        this.codeSmellAnalysisByClass = Optional.empty();
    }

    public void setCodeSmellAnalysisByClass(CodeSmellAnalysisByClass codeSmellAnalysisByClass) {
        this.codeSmellAnalysisByClass = Optional.ofNullable(codeSmellAnalysisByClass);
    }

    public void addMethodWithExcessiveParameters(int startLine, int endLine) {
        CodeAnalysisReport codeAnalysisReport = CodeAnalysisReport.builder()
                .startLine(startLine)
                .endLine(endLine)
                .message("The method has too many parameters, making it difficult to understand and use.")
                .type(CodeAnalysisReportType.EXCESSIVE_PARAMETERS)
                .build();

        addReportIfExists(codeAnalysisReport);
    }

    public void addMethodWithMagicNumbers(int startLine, int endLine) {
        CodeAnalysisReport codeAnalysisReport = CodeAnalysisReport.builder()
                .startLine(startLine)
                .endLine(endLine)
                .message("Magic numbers were detected in the method, consider using constants with descriptive names.")
                .type(CodeAnalysisReportType.MAGIC_NUMBER)
                .build();

        addReportIfExists(codeAnalysisReport);
    }

    public void addMethodWithMethodTooLong(int startLine, int endLine) {
        CodeAnalysisReport codeAnalysisReport = CodeAnalysisReport.builder()
                .startLine(startLine)
                .endLine(endLine)
                .message("The method is too long, consider breaking it down into smaller, reusable methods.")
                .type(CodeAnalysisReportType.METHOD_TOO_LONG)
                .build();

        addReportIfExists(codeAnalysisReport);
    }

    public void addMethodWithNoDuplicatedCode(int startLine, int endLine) {
        CodeAnalysisReport codeAnalysisReport = CodeAnalysisReport.builder()
                .startLine(startLine)
                .endLine(endLine)
                .message("Duplicate code was detected in the method, consider abstracting it or reusing functions.")
                .type(CodeAnalysisReportType.DUPLICATED_CODE)
                .build();

        addReportIfExists(codeAnalysisReport);
    }

    private void addReportIfExists(CodeAnalysisReport codeAnalysisReport) {
        codeSmellAnalysisByClass.ifPresent(report -> {
            report.addCodeAnalysisReport(codeAnalysisReport);
        });
    }
}
