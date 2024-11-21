package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes;
import java.util.List;
import java.util.Optional;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.AnnotationInfo;
public interface IClassIdentityCollector <T> {
    Optional<String> getClassName(T ctx);
    Optional<String> getPackageName(T ctx);
    List<String> getClassModifiers(T ctx);
    List<AnnotationInfo> getClassAnnotations(T ctx);
}
