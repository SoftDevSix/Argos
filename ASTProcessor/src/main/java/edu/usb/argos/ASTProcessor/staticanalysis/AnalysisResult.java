package edu.usb.argos.ASTProcessor.staticanalysis;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResult {
    private AnalysisType analysisType;
    private String filePath;
    List<AnalysisReport> reports;

    public AnalysisResult(AnalysisType analysisType, String filePath) {
        this.analysisType = analysisType;
        this.filePath = filePath;
        reports = new ArrayList<>();
    }

    public void addReport(AnalysisReport report) {
        reports.add(report);
    }

    public void removeReport(AnalysisReport report) {
        reports.remove(report);
    }

    public AnalysisType getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<AnalysisReport> getReports() {
        return reports;
    }
}
