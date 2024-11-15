package edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInfo;
import java.util.List;

public interface IAttributeAnalyzer<T> {

    List<AttributeInfo> visitAttribute(T ctx);

    List<String> getAttributeModifiers(AttributeInfo ctx);

    String getAttributeType(AttributeInfo ctx);

}
