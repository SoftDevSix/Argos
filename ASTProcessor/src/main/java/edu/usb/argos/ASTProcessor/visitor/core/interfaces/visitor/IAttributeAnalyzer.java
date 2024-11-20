package edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;

import java.util.List;

public interface IAttributeAnalyzer<T> {
    List<AttributeInformation> visitAttribute(T classContext);
    List<String> getAttributeModifiers(AttributeInformation classContext);
    String getAttributeType(AttributeInformation classContext);
}
