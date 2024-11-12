package edu.usb.argos.ASTProcessor.visitor.domain.entities.method;

import java.util.List;

public class DependencyInfo {
    private final List<String> methodCalls;
    private final List<String> fieldAccess;
    private final List<String> exceptions;

    public DependencyInfo(List<String> methodCalls, List<String> fieldAccess, List<String> exceptions) {
        this.methodCalls = List.copyOf(methodCalls);
        this.fieldAccess = List.copyOf(fieldAccess);
        this.exceptions = List.copyOf(exceptions);
    }

    public List<String> getMethodCalls() {
        return methodCalls;
    }

    public List<String> getFieldAccess() {
        return fieldAccess;
    }

    public List<String> getExceptions() {
        return exceptions;
    }
}
