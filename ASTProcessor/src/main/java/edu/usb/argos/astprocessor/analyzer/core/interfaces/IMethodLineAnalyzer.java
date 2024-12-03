package edu.usb.argos.astprocessor.analyzer.core.interfaces;

import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;

public interface IMethodLineAnalyzer<S> {
    int calculateMethodSize(MethodInformation<S> method);

    int getMethodStartLine(MethodInformation<S> method);

    int getMethodEndLine(MethodInformation<S> method);
}

