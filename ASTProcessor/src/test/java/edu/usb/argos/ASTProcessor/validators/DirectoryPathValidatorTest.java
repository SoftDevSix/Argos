package edu.usb.argos.ASTProcessor.validators;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.NoSuchFileException;
import edu.usb.argos.ASTProcessor.reader.infraestructure.validation.DirectoryPathValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.*;

class DirectoryPathValidatorTest {

    private Path tempDir;
    private DirectoryPathValidator pathValidator;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("testDirectory");
        pathValidator = new DirectoryPathValidator();
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testInvalidPath_NonExistentDirectory() {
        Path nonExistentPath = tempDir.resolve("nonexistentDir");

        NoSuchFileException exception = assertThrows(
            NoSuchFileException.class,
            () -> pathValidator.validatePath(nonExistentPath)
        );

        assertEquals("No found directory for: " + nonExistentPath, exception.getMessage());
    }

    @Test
    void testInvalidPath_NotADirectory() throws Exception {
        Path file = Files.createFile(tempDir.resolve("NotADirectory.java"));

        NoSuchFileException exception = assertThrows(
            NoSuchFileException.class,
            () -> pathValidator.validatePath(file)
        );

        assertEquals("The path provided is not a directory: " + file, exception.getMessage());
    }

    @Test
    void testValidPath_DirectoryExists() {
        assertDoesNotThrow(() -> pathValidator.validatePath(tempDir));
    }
}