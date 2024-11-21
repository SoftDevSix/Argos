package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes;
import java.util.List;
import java.util.Optional;

public interface IClassStructureCollector <T> {
    Optional<String> getSuperClass(T ctx);
    List<String> getImplementedInterfaces(T ctx);
}
