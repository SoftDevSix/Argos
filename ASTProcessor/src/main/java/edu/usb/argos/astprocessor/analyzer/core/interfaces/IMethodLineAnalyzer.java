package edu.usb.argos.astprocessor.analyzer.core.interfaces;

import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;

public interface IMethodLineAnalyzer<K> {
    int calculateMethodSize(MethodInformation<K> method);
    int getMethodStartLine(MethodInformation<K> method);
    int getMethodEndLine(MethodInformation<K> method);
}