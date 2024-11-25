package edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CodeElementAdapter;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import lombok.Value;

import java.util.List;

@Value
public class MethodAdapter implements CodeElementAdapter<JavaParser.StatementContext> {
    MethodInformation<JavaParser.StatementContext> methodInformation;

    @Override
    public List<Statement<JavaParser.StatementContext>> getStatements() {
        return methodInformation.getStatements();
    }

    @Override
    public String getName() {
        return methodInformation.getName();
    }
}
