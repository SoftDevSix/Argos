package edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CodeElementAdapter;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import lombok.Value;

import java.util.List;

@Value
public class ConstructorAdapter implements CodeElementAdapter<JavaParser.BlockStatementContext> {
    ConstructorInformation<JavaParser.BlockStatementContext> constructorInformation;

    @Override
    public List<JavaParser.BlockStatementContext> getStatements() {
        return constructorInformation.getBodyStatements();
    }

    @Override
    public String getName() {
        return constructorInformation.getName();
    }
}

