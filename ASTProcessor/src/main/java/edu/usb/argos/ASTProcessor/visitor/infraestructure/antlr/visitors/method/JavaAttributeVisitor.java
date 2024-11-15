package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInfo;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor.IAttributeAnalyzer;
import java.util.List;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import org.springframework.stereotype.Component;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.AttributeHandler;

@Component("javaAttributeVisitor")
public class JavaAttributeVisitor extends JavaParserBaseVisitor<List<AttributeInfo>> implements IAttributeAnalyzer<JavaParser.ClassBodyContext> {

    private final AttributeHandler attributeHandler;

    public JavaAttributeVisitor(AttributeHandler attributeHandler) {
        this.attributeHandler = attributeHandler;
    }

    @Override
    public List<AttributeInfo> visitClassBody(JavaParser.ClassBodyContext context) {
        return attributeHandler.extractAttributesFromClassBody(context);
    }

    @Override
    public List<AttributeInfo> visitAttribute(JavaParser.ClassBodyContext context) {
        return visitClassBody(context);
    }

    @Override
    public List<String> getAttributeModifiers(AttributeInfo ctx) {
        return ctx.getModifiers();
    }

    @Override
    public String getAttributeType(AttributeInfo ctx) {
        return ctx.getType();
    }
}
