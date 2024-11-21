package edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor;

import java.util.List;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ParameterInformation;

public interface IMethodAnalyzerVisitor<C, T> {
    T visitMethod(C ctx);
    List<String> getMethodModifiers(C ctx);
    String getReturnType(C ctx);
    List<ParameterInformation> getParameters(C ctx);
}
