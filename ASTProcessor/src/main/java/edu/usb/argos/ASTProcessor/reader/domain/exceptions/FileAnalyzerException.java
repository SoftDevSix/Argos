package edu.usb.argos.ASTProcessor.reader.domain.exceptions;

public class FileAnalyzerException extends RuntimeException {
    public FileAnalyzerException(String message) {
        super(message);
    }

    public FileAnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }
}
