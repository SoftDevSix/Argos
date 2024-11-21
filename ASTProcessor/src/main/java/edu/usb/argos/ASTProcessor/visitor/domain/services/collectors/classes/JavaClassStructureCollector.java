package edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes.IClassStructureCollector;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JavaClassStructureCollector implements IClassStructureCollector<ParserRuleContext> {

    @Override
    public Optional<String> getSuperClass(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> {
                    if (classCtx.EXTENDS() != null) {
                        return Optional.of(classCtx.typeType().getText());
                    }
                    return Optional.empty();
                },
                Optional.empty()
        );
    }

    @Override
    public List<String> getImplementedInterfaces(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                this::extractImplementedInterfaces,
                new ArrayList<>()
        );
    }

    private List<String> extractImplementedInterfaces(JavaParser.ClassDeclarationContext classCtx) {
        if (!hasImplementedInterfaces(classCtx)) {
            return new ArrayList<>();
        }
        return collectInterfaceNames(classCtx.typeList(0));
    }

    private boolean hasImplementedInterfaces(JavaParser.ClassDeclarationContext classCtx) {
        return classCtx.IMPLEMENTS() != null && classCtx.typeList() != null;
    }

    private List<String> collectInterfaceNames(JavaParser.TypeListContext typeList) {
        List<String> interfaces = new ArrayList<>();
        for (JavaParser.TypeTypeContext typeType : typeList.typeType()) {
            interfaces.add(typeType.getText());
        }
        return interfaces;
    }
}