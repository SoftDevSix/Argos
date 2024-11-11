package edu.usb.argos.ASTProcessor.validators;

import edu.usb.argos.ASTProcessor.infrastructure.validators.DirectoryPathValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryPathValidatorTest {

    private Path tempDir;
    private DirectoryPathValidator pathValidator;
    private TestAppender testAppender;
    private Logger logger;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("testDirectory");
        pathValidator = new DirectoryPathValidator();

        logger = (Logger) LoggerFactory.getLogger(DirectoryPathValidator.class);
        testAppender = new TestAppender();
        testAppender.start();
        logger.addAppender(testAppender);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);

        logger.detachAppender(testAppender);
        testAppender.stop();
    }

    @Test
    void testInvalidPath_NonExistentDirectory() {
        Path nonExistentPath = tempDir.resolve("nonexistentDir");

        boolean isValid = pathValidator.isValidPath(nonExistentPath);

        assertFalse(isValid);
        assertTrue(testAppender.getMessages().stream().anyMatch(
                msg -> msg.contains("No found directory for: " + nonExistentPath)
        ));
    }

    @Test
    void testInvalidPath_NotADirectory() throws Exception {
        Path file = Files.createFile(tempDir.resolve("NotADirectory.java"));

        boolean isValid = pathValidator.isValidPath(file);

        assertFalse(isValid);
        assertTrue(testAppender.getMessages().stream().anyMatch(
                msg -> msg.contains("The path provided is not a directory: " + file)
        ));
    }

    @Test
    void testValidPath_DirectoryExists() {
        boolean isValid = pathValidator.isValidPath(tempDir);

        assertTrue(isValid);
        assertTrue(testAppender.getMessages().isEmpty());
    }
}
