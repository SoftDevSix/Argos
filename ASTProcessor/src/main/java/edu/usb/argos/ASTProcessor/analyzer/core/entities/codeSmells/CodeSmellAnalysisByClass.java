package edu.usb.argos.ASTProcessor.analyzer.core.entities.codeSmells;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CodeSmellAnalysisByClass {
    private String classFilePath;
    private List<CodeAnalysisReport> codeAnalysis;

    public CodeSmellAnalysisByClass(String classFilePath) {
        this.classFilePath = classFilePath;
        codeAnalysis = new ArrayList<>();
    }
    
    public void addCodeAnalysisReport(CodeAnalysisReport codeAnalysisReport) {
        this.codeAnalysis.add(codeAnalysisReport);
    }
}
