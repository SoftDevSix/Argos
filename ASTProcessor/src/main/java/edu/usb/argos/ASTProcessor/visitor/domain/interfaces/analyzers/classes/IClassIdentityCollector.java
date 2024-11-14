package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes;
import java.util.List;

import edu.usb.argos.ASTProcessor.visitor.domain.entities.classes.AnnotationInfo;
public interface IClassIdentityCollector <T> {

    String getClassName(T ctx);
    String getPackageName(T ctx);
    List<String> getClassModifiers(T ctx);
    List<AnnotationInfo> getClassAnnotations(T ctx);

}
