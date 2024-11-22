package edu.usb.argos.ASTProcessor.lexer.errorHandler.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LexerFileProcessingExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "This is a test message";
        LexerFileProcessingException exception = new LexerFileProcessingException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithNullMessage() {
        LexerFileProcessingException exception = new LexerFileProcessingException(null);
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithEmptyMessage() {
        String expectedMessage = "";
        LexerFileProcessingException exception = new LexerFileProcessingException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String expectedMessage = "This is a test message";
        Throwable cause = new Throwable("This is the cause");
        LexerFileProcessingException exception = new LexerFileProcessingException(expectedMessage, cause);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
