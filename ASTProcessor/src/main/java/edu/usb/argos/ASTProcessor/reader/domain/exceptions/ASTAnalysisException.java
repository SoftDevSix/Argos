package edu.usb.argos.ASTProcessor.reader.domain.exceptions;

public class ASTAnalysisException extends RuntimeException {
    public ASTAnalysisException(String message) {
        super(message);
    }

    public ASTAnalysisException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
