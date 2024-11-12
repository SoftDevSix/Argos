package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes;

import java.util.List;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.classes.ClassInfo;

public interface IClassAnalyzerVisitor<T> {

    ClassInfo visitClassDeclaration(T ctx);
}
