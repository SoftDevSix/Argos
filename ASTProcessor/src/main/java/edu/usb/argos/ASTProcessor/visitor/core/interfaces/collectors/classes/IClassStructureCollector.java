package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes;
import java.util.List;

public interface IClassStructureCollector <T> {

    String getSuperClass(T ctx);
    List<String> getImplementedInterfaces(T ctx);

}
