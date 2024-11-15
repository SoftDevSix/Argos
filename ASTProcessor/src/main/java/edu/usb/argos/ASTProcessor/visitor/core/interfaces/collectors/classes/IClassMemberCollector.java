package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInfo;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInfo;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInfo;

import java.util.List;

public interface IClassMemberCollector <T> {

    List<MethodInfo> getClassMethods(T ctx);
     List<AttributeInfo> getClassAttributes(T ctx);
     List<ConstructorInfo> getClassConstructors(T ctx);
}
