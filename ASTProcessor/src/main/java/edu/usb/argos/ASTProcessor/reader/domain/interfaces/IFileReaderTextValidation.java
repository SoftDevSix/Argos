package edu.usb.argos.ASTProcessor.reader.domain.interfaces;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileReaderException;

public interface IFileReaderTextValidation {
    void validateFileReaderByText(String content) throws FileReaderException;
}
