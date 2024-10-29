package edu.usb.argos.ASTProcessor.reader.interfaces;

import edu.usb.argos.ASTProcessor.reader.exceptions.FileAnalyzerException;

public interface IValidationStrategy<T> {
    void validate(T input) throws FileAnalyzerException;
}
