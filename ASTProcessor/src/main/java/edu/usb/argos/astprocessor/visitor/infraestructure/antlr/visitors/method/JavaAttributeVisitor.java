package edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method;

import edu.usb.argos.astprocessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.astprocessor.visitor.core.interfaces.visitor.IAttributeAnalyzer;
import java.util.List;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.antlr.JavaParserBaseVisitor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("javaAttributeVisitor")
@AllArgsConstructor
public class JavaAttributeVisitor extends JavaParserBaseVisitor<List<AttributeInformation>> implements IAttributeAnalyzer<JavaParser.ClassBodyContext> {

    private final AttributeHandler attributeHandler;

    @Override
    public List<AttributeInformation> visitClassBody(JavaParser.ClassBodyContext context) {
        return attributeHandler.extractAttributesFromClassBody(context);
    }

    @Override
    public List<AttributeInformation> visitAttribute(JavaParser.ClassBodyContext classContext) {
        return visitClassBody(classContext);
    }

    @Override
    public List<String> getAttributeModifiers(AttributeInformation classContext) {
        return classContext.getModifiers();
    }

    @Override
    public String getAttributeType(AttributeInformation classContext) {
        return classContext.getType();
    }
}
