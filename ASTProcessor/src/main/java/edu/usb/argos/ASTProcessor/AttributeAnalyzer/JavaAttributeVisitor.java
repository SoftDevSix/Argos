package main.java.edu.usb.argos.ASTProcessor.AttributeAnalyzer;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import main.java.edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities.AttributeInfo;
import main.java.edu.usb.argos.ASTProcessor.AttributeAnalyzer.Interfaces.IAttributeAnalyzer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;

public class JavaAttributeVisitor extends JavaParserBaseVisitor<AttributeInfo>
        implements IAttributeAnalyzer<ParserRuleContext> {
    @Override
    public AttributeInfo visitAttribute(ParserRuleContext context) {
        return validateAndExecute(context, attributeContext -> {
            String name = getAttributeName(attributeContext);
            String type = getAttributeType(attributeContext);
            List<String> modifiers = getAttributeModifiers(attributeContext);

            return new AttributeInfo(name, type, modifiers);
        }, null);
    }

    @Override
    public List<String> getAttributeModifiers(ParserRuleContext context) {
        return validateAndExecute(context, attributeContext -> {
            List<String> modifiers = new ArrayList<>();
            for (int i = 0; i < attributeContext.getChildCount(); i++) {
                String tokenText = attributeContext.getChild(i).getText();
                if (isModifier(tokenText)) {
                    modifiers.add(tokenText);
                    tokenText = "";
                } else {
                    break;
                }
            }
            return modifiers;
        }, new ArrayList<>());
    }

    @Override
    public String getAttributeType(ParserRuleContext context) {
        return validateAndExecute(context, attributeContext -> {
            System.out.println(attributeContext.getChildCount());
            for (int i = 0; i < attributeContext.getChildCount(); i++) {
                String tokenText = attributeContext.getChild(i).getText();
                System.out.println(tokenText);
                if (isModifier(tokenText)) {
                    continue;
                }

                if (isPrimitiveType(tokenText)) {
                    return tokenText;
                }
            }
            return "";
        }, "");
    }

    private boolean isPrimitiveType(String tokenText) {
        return switch (tokenText) {
            case "int", "double", "float", "boolean", "char", "byte", "short", "long" -> true;
            default -> false;
        };
    }

    public String getAttributeName(ParserRuleContext context) {
        return validateAndExecute(context, attributeContext -> {
            boolean typeFound = false;
            for (int i = 0; i < attributeContext.getChildCount(); i++) {
                String tokenText = attributeContext.getChild(i).getText();
                if (typeFound) {
                    return tokenText;
                }
                if (!isModifier(tokenText)) {
                    typeFound = true;
                }
            }
            return "";
        }, "");
    }

    private <T> T validateAndExecute(ParserRuleContext context,
            Function<JavaParser.FieldDeclarationContext, T> extractor,
            T defaultValue) {
        if (context instanceof JavaParser.FieldDeclarationContext attributeContext) {
            return extractor.apply(attributeContext);
        }
        return defaultValue;
    }

    private boolean isModifier(String tokenText) {
        return tokenText.equals("public") || tokenText.equals("private") || tokenText.equals("protected") ||
                tokenText.equals("static") || tokenText.equals("final") || tokenText.equals("abstract") ||
                tokenText.equals("volatile") || tokenText.equals("transient") || tokenText.equals("synchronized");
    }
}
