package edu.usb.argos.astprocessor.reader.domain.exceptions;

public class ASTAnalysisException extends RuntimeException {
    public ASTAnalysisException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
