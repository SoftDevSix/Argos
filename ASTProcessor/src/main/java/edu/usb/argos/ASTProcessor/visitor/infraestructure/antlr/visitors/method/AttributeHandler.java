package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("attributeHandlerOne")
public class AttributeHandler {

    public List<AttributeInformation> extractAttributesFromClassBody(JavaParser.ClassBodyContext ctx) {
        List<AttributeInformation> attributes = new ArrayList<>();

        for (JavaParser.ClassBodyDeclarationContext bodyCtx : ctx.classBodyDeclaration()) {
            JavaParser.MemberDeclarationContext memberCtx = bodyCtx.memberDeclaration();

            if (memberCtx != null && memberCtx.fieldDeclaration() != null) {
                Optional<AttributeInformation> attributeInfo = extractFieldInfo(memberCtx.fieldDeclaration(), bodyCtx);
                attributeInfo.ifPresent(attributes::add);
            }
        }
        return attributes;
    }

    private Optional<AttributeInformation> extractFieldInfo(JavaParser.FieldDeclarationContext fieldCtx, JavaParser.ClassBodyDeclarationContext bodyCtx) {
        String fieldType = fieldCtx.typeType().getText();
        List<String> modifiers = extractModifiers(bodyCtx);

        for (JavaParser.VariableDeclaratorContext varCtx : fieldCtx.variableDeclarators().variableDeclarator()) {
            String varName = varCtx.variableDeclaratorId().getText();
            AttributeInformation attributeInformation = AttributeInformation.builder()
                    .name(varName)
                    .type(fieldType)
                    .modifiers(modifiers)
                    .build();

            return Optional.of(attributeInformation);
        }

        return Optional.empty();
    }

    private List<String> extractModifiers(JavaParser.ClassBodyDeclarationContext ctx) {
        List<String> modifiers = new ArrayList<>();

        for (JavaParser.ModifierContext modCtx : ctx.modifier()) {
            modifiers.add(modCtx.getText());
        }

        return modifiers;
    }
}
