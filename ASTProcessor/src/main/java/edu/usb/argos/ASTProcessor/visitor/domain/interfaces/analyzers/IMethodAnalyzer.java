package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;

public interface IMethodAnalyzer<T> {
    T analyze(JavaParser.MethodDeclarationContext ctx);
}
