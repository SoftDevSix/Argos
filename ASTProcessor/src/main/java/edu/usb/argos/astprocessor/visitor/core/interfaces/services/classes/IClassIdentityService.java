package edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes;
import java.util.List;
import java.util.Optional;

import edu.usb.argos.astprocessor.visitor.core.entities.classes.AnnotationInformation;
public interface IClassIdentityService<T> {
    Optional<String> getClassName(T ctx);
    Optional<String> getPackageName(T ctx);
    List<String> getClassModifiers(T ctx);
    List<AnnotationInformation> getClassAnnotations(T ctx);
}