package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes;
import java.util.List;

public interface IClassStructureCollector <T> {

    String getSuperClass(T ctx);
    List<String> getImplementedInterfaces(T ctx);

}
