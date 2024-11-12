package edu.usb.argos.ASTProcessor.application.analyzers;

import edu.usb.argos.ASTProcessor.application.exceptions.FileAnalyzerException;

public interface IFileAnalyzer<T, F> {
    F readFile(T codePath) throws FileAnalyzerException;
}
