package edu.usb.argos.astprocessor.visitor.core.services.classes;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes.IClassStructureService;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.shared.validation.ContextValidator;
import lombok.Generated;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Generated
public class JavaClassStructureService implements IClassStructureService<ParserRuleContext> {

    @Override
    public Optional<String> getSuperClass(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                this::extractSuperClass,
                Optional.empty()
        );
    }

    private Optional<String> extractSuperClass(JavaParser.ClassDeclarationContext classCtx) {
        if (hasSuperClass(classCtx)) {
            return Optional.of(getSuperClassName(classCtx));
        }
        return Optional.empty();
    }

    private boolean hasSuperClass(JavaParser.ClassDeclarationContext classCtx) {
        return Optional.ofNullable(classCtx.EXTENDS())
                .isPresent() &&
                Optional.ofNullable(classCtx.typeType())
                        .isPresent();
    }

    private String getSuperClassName(JavaParser.ClassDeclarationContext classCtx) {
        return classCtx.typeType().getText();
    }

    @Override
    public List<String> getImplementedInterfaces(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                this::extractImplementedInterfaces,
                Collections.emptyList()
        );
    }

    private List<String> extractImplementedInterfaces(JavaParser.ClassDeclarationContext classCtx) {
        if (!hasImplementedInterfaces(classCtx)) {
            return Collections.emptyList();
        }
        return collectInterfaceNames(classCtx.typeList(0));
    }

    private boolean hasImplementedInterfaces(JavaParser.ClassDeclarationContext classCtx) {
        return Optional.ofNullable(classCtx.IMPLEMENTS())
                .isPresent() &&
                Optional.ofNullable(classCtx.typeList())
                        .map(typeLists -> !typeLists.isEmpty())
                        .orElse(false);
    }

    private List<String> collectInterfaceNames(JavaParser.TypeListContext typeList) {
        List<String> interfaces = new ArrayList<>();
        for (JavaParser.TypeTypeContext typeType : typeList.typeType()) {
            interfaces.add(typeType.getText());
        }
        return interfaces;
    }
}