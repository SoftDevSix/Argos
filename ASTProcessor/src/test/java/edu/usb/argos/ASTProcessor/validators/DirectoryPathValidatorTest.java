package edu.usb.argos.ASTProcessor.validators;

import edu.usb.argos.ASTProcessor.infrastructure.validators.DirectoryPathValidator;
import edu.usb.argos.ASTProcessor.application.logging.IAppLogger;
import edu.usb.argos.ASTProcessor.application.validators.IPathValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class DirectoryPathValidatorTest {

    private Path tempDir;
    private IPathValidator pathValidator;
    private IAppLogger mockLogger;
    private String loggedMessage;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("testDirectory");
        mockLogger = mock(IAppLogger.class);

        doAnswer(invocation -> {
            loggedMessage = invocation.getArgument(0);
            return null;
        }).when(mockLogger).error(anyString(), any(Throwable.class));

        pathValidator = new DirectoryPathValidator(mockLogger);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testInvalidPath_NonExistentDirectory() {
        Path nonExistentPath = tempDir.resolve("nonexistentDir");

        boolean isValid = pathValidator.isValidPath(nonExistentPath);

        assertFalse(isValid);
        assertNotNull(loggedMessage);
        assertTrue(loggedMessage.contains("No found directory for"));
    }

    @Test
    void testInvalidPath_NotADirectory() throws IOException {
        Path file = Files.createFile(tempDir.resolve("NotADirectory.java"));

        boolean isValid = pathValidator.isValidPath(file);

        assertFalse(isValid);
        assertNotNull(loggedMessage);
        assertTrue(loggedMessage.contains("The path provided is not a directory"));
    }

    @Test
    void testValidPath_DirectoryExists() {
        boolean isValid = pathValidator.isValidPath(tempDir);

        assertTrue(isValid);
        assertNull(loggedMessage);
    }
}