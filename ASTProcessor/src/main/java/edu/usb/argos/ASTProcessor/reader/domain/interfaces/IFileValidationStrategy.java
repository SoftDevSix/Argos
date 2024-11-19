package edu.usb.argos.ASTProcessor.reader.domain.interfaces;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;

public interface IFileValidationStrategy<T> {
    void validate(T input) throws FileAnalyzerException;
}
