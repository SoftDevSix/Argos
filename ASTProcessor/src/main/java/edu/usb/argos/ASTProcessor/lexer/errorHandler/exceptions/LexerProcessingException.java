package edu.usb.argos.ASTProcessor.lexer.errorHandler.exceptions;

import java.io.IOException;

public class LexerProcessingException extends RuntimeException {
    public LexerProcessingException(String message) {
        super(message);
    }

    public LexerProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

