package edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers;

import java.util.List;

public interface CodeElementAdapter<S> {
    List<S> getStatements();
    String getName();
}
