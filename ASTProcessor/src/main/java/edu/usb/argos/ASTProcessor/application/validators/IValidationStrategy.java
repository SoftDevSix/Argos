package edu.usb.argos.ASTProcessor.application.validators;

import edu.usb.argos.ASTProcessor.application.exceptions.FileAnalyzerException;

public interface IValidationStrategy<T> {
    void validate(T input) throws FileAnalyzerException;
}
