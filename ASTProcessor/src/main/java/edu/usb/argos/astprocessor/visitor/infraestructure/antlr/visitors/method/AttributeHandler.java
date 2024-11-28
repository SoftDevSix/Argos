package edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.astprocessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("attributeHandlerOne")
public class AttributeHandler {

    public List<AttributeInformation> extractAttributesFromClassBody(JavaParser.ClassBodyContext context) {
        List<AttributeInformation> attributes = new ArrayList<>();

        for (JavaParser.ClassBodyDeclarationContext bodyContext : context.classBodyDeclaration()) {
            JavaParser.MemberDeclarationContext memberContext = bodyContext.memberDeclaration();

            if (memberContext != null && memberContext.fieldDeclaration() != null) {
                Optional<AttributeInformation> attributeInfo = extractFieldInfo(memberContext.fieldDeclaration(), bodyContext);
                attributeInfo.ifPresent(attributes::add);
            }
        }
        return attributes;
    }

    private Optional<AttributeInformation> extractFieldInfo(JavaParser.FieldDeclarationContext fieldContext, JavaParser.ClassBodyDeclarationContext bodyContext) {
        String fieldType = fieldContext.typeType().getText();
        List<String> modifiers = extractModifiers(bodyContext);

        if (fieldContext.variableDeclarators().variableDeclarator().isEmpty()) {
            return Optional.empty();
        }

        JavaParser.VariableDeclaratorContext varCtx = fieldContext.variableDeclarators().variableDeclarator().get(0);
        String varName = varCtx.variableDeclaratorId().getText();
        Optional<String> value = Optional.ofNullable(varCtx.variableInitializer())
                .map(JavaParser.VariableInitializerContext::getText);

        AttributeInformation attributeInformation = AttributeInformation.builder()
                .name(varName)
                .type(fieldType)
                .modifiers(modifiers)
                .value(value)
                .build();

        return Optional.of(attributeInformation);
    }

    private List<String> extractModifiers(JavaParser.ClassBodyDeclarationContext context) {
        List<String> modifiers = new ArrayList<>();

        for (JavaParser.ModifierContext modifierContext : context.modifier()) {
            modifiers.add(modifierContext.getText());
        }

        return modifiers;
    }
}
