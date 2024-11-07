package edu.usb.argos.ASTProcessor.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import edu.usb.argos.ASTProcessor.infrastructure.utils.SLF4JAppLogger;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SLF4JAppLoggerTest {

    private SLF4JAppLogger appLogger;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        appLogger = new SLF4JAppLogger(SLF4JAppLogger.class);
    }

    @Test
    void testInfoLogsMessage() {
        String expectedMessage = "Info level log message";
        appLogger.info(expectedMessage);
        
        assertTrue(outContent.toString().contains(expectedMessage));
        outContent.reset();
    }

    @Test
    void testDebugLogsMessage() {
        String expectedMessage = "Debug level log message";
        appLogger.debug(expectedMessage);

        assertTrue(outContent.toString().contains(expectedMessage));
        outContent.reset();
    }

    @Test
    void testErrorLogsMessage() {
        String expectedMessage = "Error level log message";
        appLogger.error(expectedMessage, new RuntimeException("Simulated exception"));

        assertTrue(outContent.toString().contains(expectedMessage));
        outContent.reset();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalSystemOut);
    }
}
