package edu.usb.argos.ASTProcessor.lexer.errorHandler.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AntlrExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "This is a test message";
        AntlrException exception = new AntlrException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithNullMessage() {
        AntlrException exception = new AntlrException(null);
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithEmptyMessage() {
        String expectedMessage = "";
        AntlrException exception = new AntlrException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
