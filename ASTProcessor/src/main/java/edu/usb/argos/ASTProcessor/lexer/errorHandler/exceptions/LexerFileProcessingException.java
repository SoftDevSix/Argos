package edu.usb.argos.ASTProcessor.lexer.errorHandler.exceptions;

public class LexerFileProcessingException extends RuntimeException {
    public LexerFileProcessingException(String message) {
        super(message);
    }

    public LexerFileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

