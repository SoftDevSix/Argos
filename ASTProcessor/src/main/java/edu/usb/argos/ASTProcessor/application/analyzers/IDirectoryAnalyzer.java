package edu.usb.argos.ASTProcessor.application.analyzers;

import java.util.List;

public interface IDirectoryAnalyzer<T, F> {
    List<F> analyzeDirectory(T sourceCode);
}
