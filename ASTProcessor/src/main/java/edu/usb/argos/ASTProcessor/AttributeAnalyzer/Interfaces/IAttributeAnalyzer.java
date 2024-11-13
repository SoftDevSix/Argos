package edu.usb.argos.ASTProcessor.AttributeAnalyzer.Interfaces;

import edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities.AttributeInfo;

import java.util.List;

public interface IAttributeAnalyzer<T> {

    List<AttributeInfo> visitAttribute(T ctx);

    List<String> getAttributeModifiers(AttributeInfo ctx);

    String getAttributeType(AttributeInfo ctx);

}
