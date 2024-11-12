package edu.usb.argos.ASTProcessor.application.exceptions;

public class FileAnalyzerException extends Exception {
    public FileAnalyzerException(String message) {
        super(message);
    }

    public FileAnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }
}
