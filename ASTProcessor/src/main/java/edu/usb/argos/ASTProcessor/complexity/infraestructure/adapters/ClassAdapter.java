package edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters;

import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.CodeElementAdapter;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassMembers;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParser.StatementContext;
import lombok.Value;

import java.util.List;
import java.util.ArrayList;

@Value
public class ClassAdapter implements CodeElementAdapter<JavaParser.StatementContext> {
    private ClassInformation<JavaParser.StatementContext> classInformation;

    public ClassMembers<JavaParser.StatementContext> getClassMembers() {
        return classInformation.getMembers();
    }

    @Override
    public String getName() {
        return classInformation.getIdentity().getName().orElse("Unknown CLass");
    }

    @Override
    public List<StatementContext> getStatements() {
        List<JavaParser.StatementContext> statements = new ArrayList<>();

        if(classInformation.getMembers() != null && classInformation.getMembers().getMethods() != null) {
            classInformation.getMembers().
                    getMethods().forEach(method -> statements.addAll(method.getStatements()));
        }

        if(classInformation.getMembers() != null && classInformation.getMembers().getConstructors() != null) {
            classInformation.getMembers().
                    getConstructors().forEach(constructor -> statements.addAll(constructor.getBodyStatements()));
        }

        return statements;
    }
}
