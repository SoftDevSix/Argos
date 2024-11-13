package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes;

import edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities.AttributeInfo;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.MethodInfo;

import java.util.List;

public interface IClassMemberCollector <T> {

    List<MethodInfo> getClassMethods(T ctx);
     List<AttributeInfo> getClassAttributes(T ctx);
    // List<ConstructorInfo> getClassConstructors(T ctx);
}
