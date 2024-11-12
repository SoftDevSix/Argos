package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.*;

import java.util.List;

public interface IMethodAnalyzerVisitor<T> {
    MethodInfo visitMethod(T ctx);
    CodeMetrics getCodeMetrics(T ctx);
    ComplexityMetrics getComplexityMetrics(T ctx);
    DependencyInfo getDependencyInfo(T ctx);
    List<String> getMethodModifiers(T ctx);
    String getReturnType(T ctx);
    List<ParameterInfo> getParameters(T ctx);
}
