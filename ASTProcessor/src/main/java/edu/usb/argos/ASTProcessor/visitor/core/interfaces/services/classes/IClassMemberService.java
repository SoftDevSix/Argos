package edu.usb.argos.ASTProcessor.visitor.core.interfaces.services.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;

import java.util.List;

public interface IClassMemberService<T, S> {
    List<MethodInformation<S>> getClassMethods(T ctx);
    List<AttributeInformation> getClassAttributes(T ctx);
    List<ConstructorInformation<S>> getClassConstructors(T ctx);
}