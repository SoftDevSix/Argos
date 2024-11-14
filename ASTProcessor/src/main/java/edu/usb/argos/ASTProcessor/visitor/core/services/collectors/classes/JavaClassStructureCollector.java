package edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassStructureCollector;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

public class JavaClassStructureCollector implements IClassStructureCollector<ParserRuleContext> {

    @Override
    public String getSuperClass(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> {
                    if (classCtx.EXTENDS() != null) {
                        return classCtx.typeType().getText();
                    }
                    return null;
                },
                null
        );
    }

    @Override
    public List<String> getImplementedInterfaces(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> {
                    List<String> interfaces = new ArrayList<>();
                    if (classCtx.IMPLEMENTS() != null && classCtx.typeList() != null) {
                        JavaParser.TypeListContext typeList = classCtx.typeList(0);
                        for (JavaParser.TypeTypeContext typeType : typeList.typeType()) {
                            interfaces.add(typeType.getText());
                        }
                    }
                    return interfaces;
                },
                new ArrayList<>()
        );
    }
}