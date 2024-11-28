package edu.usb.argos.astprocessor.reader.domain.interfaces;

import edu.usb.argos.astprocessor.reader.domain.exceptions.FileAnalyzerException;

public interface IFileValidationStrategy<T> {
    void validate(T input) throws FileAnalyzerException;
}
