package edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes;
import java.util.List;
import java.util.Optional;

public interface IClassStructureService<T> {
    Optional<String> getSuperClass(T ctx);
    List<String> getImplementedInterfaces(T ctx);
}