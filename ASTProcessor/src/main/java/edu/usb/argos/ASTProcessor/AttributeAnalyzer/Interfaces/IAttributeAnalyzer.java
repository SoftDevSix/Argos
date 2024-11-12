package main.java.edu.usb.argos.ASTProcessor.AttributeAnalyzer.Interfaces;

import java.util.List;

import main.java.edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities.AttributeInfo;

public interface IAttributeAnalyzer<T> {
    AttributeInfo visitAttribute(T ctx);
    List<String> getAttributeModifiers(T ctx);
    String getAttributeType(T ctx);
}
