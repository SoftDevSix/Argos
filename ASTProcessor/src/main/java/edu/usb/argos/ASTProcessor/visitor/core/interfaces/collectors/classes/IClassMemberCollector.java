package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInfo;

import java.util.List;

public interface IClassMemberCollector <T> {

    List<MethodInfo> getClassMethods(T ctx);
    List<AttributeInformation> getClassAttributes(T ctx);
    List<ConstructorInformation> getClassConstructors(T ctx);
}
