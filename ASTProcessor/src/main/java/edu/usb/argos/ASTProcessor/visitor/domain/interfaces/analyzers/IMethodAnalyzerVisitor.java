package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.*;

import java.util.List;

public interface IMethodAnalyzerVisitor<C, T> {
    T visitMethod(C ctx);
    List<String> getMethodModifiers(C ctx);
    String getReturnType(C ctx);
    List<ParameterInfo> getParameters(C ctx);
}
