package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ClassMembers {
    List<MethodInformation<Object>> methods;
    List<AttributeInformation> attributes;
    List<ConstructorInformation<Object>> constructors;
}
