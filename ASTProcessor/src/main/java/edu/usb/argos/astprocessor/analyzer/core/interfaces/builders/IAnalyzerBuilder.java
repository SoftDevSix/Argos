package edu.usb.argos.astprocessor.analyzer.core.interfaces.builders;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;

public interface IAnalyzerBuilder<T> {
    ICodeSmellNodeAnalyzer<T> buildAnalyzer();
}
