package edu.usb.argos.staticanalysis.service;

import edu.usb.argos.staticanalysis.result.AnalysisResult;

import java.util.List;

public interface IAnalyzerService {
    List<AnalysisResult> analyzeProject();
}
