package edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.*;

import java.util.List;

public interface IMethodAnalyzerVisitor<C, T> {
    T visitMethod(C ctx);
    List<String> getMethodModifiers(C ctx);
    String getReturnType(C ctx);
    List<ParameterInformation> getParameters(C ctx);
}
