package edu.usb.argos.ASTProcessor.analyzer.core.entities.codeSmells;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CodeSmellAnalysisByClass {
    private String classFilePath;
    private List<CodeAnalysisReport> codeAnalysis;

    public void addCodeAnalysisReport(CodeAnalysisReport codeAnalysisReport) {
        this.codeAnalysis.add(codeAnalysisReport);
    }
}
