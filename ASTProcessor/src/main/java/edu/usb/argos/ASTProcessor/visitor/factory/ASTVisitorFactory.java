package edu.usb.argos.ASTProcessor.visitor.factory;

import edu.usb.argos.ASTProcessor.visitor.interfaces.IMethodAnalyzerVisitor;

public interface ASTVisitorFactory
{
    IMethodAnalyzerVisitor createMethodVisitor();
    // Here class visitor
    // Here attribute visitor
}
