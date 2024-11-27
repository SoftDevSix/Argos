package edu.usb.argos.ASTProcessor.staticanalysis.analysisresult;

import edu.usb.argos.ASTProcessor.staticanalysis.AnalysisType;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class AnalysisResult {
    private AnalysisType analysisType;
    private String filePath;
    private List<AnalysisReport> reports;

    public AnalysisResult(AnalysisType analysisType, String filePath, List<AnalysisReport> reports) {
        this.analysisType = analysisType;
        this.filePath = filePath;
        this.reports = reports;
    }

    public void addReport(AnalysisReport report) {
        this.reports.add(report);
    }

    public void removeReport(AnalysisReport report) {
        this.reports.remove(report);
    }
}
