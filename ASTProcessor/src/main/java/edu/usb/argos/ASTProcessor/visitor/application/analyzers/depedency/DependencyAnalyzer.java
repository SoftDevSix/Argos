package edu.usb.argos.ASTProcessor.visitor.application.analyzers.depedency;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.DependencyInfo;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IMethodAnalyzer;

public class DependencyAnalyzer implements IMethodAnalyzer<DependencyInfo> {
    @Override
    public DependencyInfo analyze(JavaParser.MethodDeclarationContext ctx) {
        DependencyVisitor visitor = new DependencyVisitor();
        ctx.accept(visitor);
        return visitor.getDependencyInfo();
    }
}
