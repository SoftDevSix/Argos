package edu.usb.argos.ASTProcessor.reader.domain.interfaces;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;

public interface IFileAnalyzer<T, F> {
    F readFile(T codePath) throws FileAnalyzerException;
}
