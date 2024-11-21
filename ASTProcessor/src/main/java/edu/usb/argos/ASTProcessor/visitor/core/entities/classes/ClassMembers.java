package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ClassMembers {
    private final List<MethodInformation> methods;
    private final List<AttributeInformation> attributes;
    private final List<ConstructorInformation> constructors;
}
