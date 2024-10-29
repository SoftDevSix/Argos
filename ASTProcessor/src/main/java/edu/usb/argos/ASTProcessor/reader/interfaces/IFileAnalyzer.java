package edu.usb.argos.ASTProcessor.reader.interfaces;

import edu.usb.argos.ASTProcessor.reader.exceptions.FileAnalyzerException;

public interface IFileAnalyzer<T, F> {
    F readFile(T codePath) throws FileAnalyzerException;
}
