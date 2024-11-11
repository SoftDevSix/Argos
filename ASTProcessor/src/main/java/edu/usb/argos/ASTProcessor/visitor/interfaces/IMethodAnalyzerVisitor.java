package edu.usb.argos.ASTProcessor.visitor.interfaces;

import edu.usb.argos.ASTProcessor.visitor.models.method.MethodInfo;
import edu.usb.argos.ASTProcessor.visitor.models.method.ParameterInfo;

import java.util.List;

public interface IMethodAnalyzerVisitor<T> {
    MethodInfo visitMethod(T ctx);
    int getMethodLines(T ctx);
    List<String> getMethodModifiers(T ctx);
    String getReturnType(T ctx);
    List<ParameterInfo> getParameters(T ctx);
}
