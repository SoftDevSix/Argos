package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInfo;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClassMembers {

    private final List<MethodInfo> methods;
    private final List<AttributeInfo> attributes;
    private final List<ConstructorInfo> constructors;

}
