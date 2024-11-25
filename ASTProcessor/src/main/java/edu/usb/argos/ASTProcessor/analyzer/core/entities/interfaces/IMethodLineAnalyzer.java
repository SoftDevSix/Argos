package edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;

public interface IMethodLineAnalyzer<K> {
    int calculateMethodSize(MethodInformation<K> method);
    int getMethodStartLine(MethodInformation<K> method);
    int getMethodEndLine(MethodInformation<K> method);
}
