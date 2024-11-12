package main.java.edu.usb.argos.ASTProcessor.FileReaderByText.Interfaces;

import main.java.edu.usb.argos.ASTProcessor.FileReaderByText.FileReaderHandlers.FileReaderException;

public interface IFileReaderTextValidation {
    void validateFileReaderByText(String content) throws FileReaderException;
}
