package edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CodeElementAdapter;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class ConstructorAdapter implements CodeElementAdapter<JavaParser.StatementContext> {
    ConstructorInformation<JavaParser.StatementContext> constructorInformation;

    @Override
    public List<Statement<JavaParser.StatementContext>> getStatements() {
        return constructorInformation.getBodyStatements().stream()
                .map(stmt -> new Statement<JavaParser.StatementContext>() {
                    @Override
                    public JavaParser.StatementContext getNode() {
                        return stmt;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return constructorInformation.getName();
    }
}