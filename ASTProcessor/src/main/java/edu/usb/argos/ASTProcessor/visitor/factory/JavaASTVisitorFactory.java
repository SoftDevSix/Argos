package edu.usb.argos.ASTProcessor.visitor.factory;

import edu.usb.argos.ASTProcessor.visitor.implementation.method.JavaMethodVisitor;
import edu.usb.argos.ASTProcessor.visitor.interfaces.IMethodAnalyzerVisitor;

public class JavaASTVisitorFactory implements ASTVisitorFactory {
    @Override
    public IMethodAnalyzerVisitor createMethodVisitor() {
        return new JavaMethodVisitor();
    }

    // Here should be class visitor
    // Here should be attribute visitor
}
