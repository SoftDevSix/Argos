package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInfo;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("attributeHandlerOne")
public class AttributeHandler {

    public List<AttributeInfo> extractAttributesFromClassBody(JavaParser.ClassBodyContext ctx) {
        List<AttributeInfo> attributes = new ArrayList<>();

        for (JavaParser.ClassBodyDeclarationContext bodyCtx : ctx.classBodyDeclaration()) {
            JavaParser.MemberDeclarationContext memberCtx = bodyCtx.memberDeclaration();

            if (memberCtx != null && memberCtx.fieldDeclaration() != null) {
                AttributeInfo attributeInfo = extractFieldInfo(memberCtx.fieldDeclaration(), bodyCtx);
                if (attributeInfo != null) {
                    attributes.add(attributeInfo);
                }
            }
        }

        return attributes;
    }

    private AttributeInfo extractFieldInfo(JavaParser.FieldDeclarationContext fieldCtx, JavaParser.ClassBodyDeclarationContext bodyCtx) {
        String fieldType = fieldCtx.typeType().getText();
        List<String> modifiers = extractModifiers(bodyCtx);

        for (JavaParser.VariableDeclaratorContext varCtx : fieldCtx.variableDeclarators().variableDeclarator()) {
            String varName = varCtx.variableDeclaratorId().getText();
            return new AttributeInfo(varName, fieldType, modifiers);
        }
        return null;
    }

    private List<String> extractModifiers(JavaParser.ClassBodyDeclarationContext ctx) {
        List<String> modifiers = new ArrayList<>();
        for (JavaParser.ModifierContext modCtx : ctx.modifier()) {
            modifiers.add(modCtx.getText());
        }
        return modifiers;
    }

}
