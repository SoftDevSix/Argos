package edu.usb.argos.ASTProcessor.validators;

import edu.usb.argos.ASTProcessor.application.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.infrastructure.validators.PathValidationStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PathValidationStrategyTest {
    private PathValidationStrategy validator;

    @TempDir
    private Path tempDir;

    @BeforeEach
    public void setUp() {
        validator = new PathValidationStrategy();
    }

    @Test
    public void shouldValidateValidJavaFile() throws IOException {
        Path validFile = createValidJavaFile();
        assertDoesNotThrow(() -> validator.validate(validFile));
    }

    @Test
    public void shouldThrowExceptionForNullPath() {
        FileAnalyzerException exception = assertThrows(
                FileAnalyzerException.class,
                () -> validator.validate(null)
        );

        assertEquals("File path can't be null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionForNonJavaFile() throws IOException {
        Path nonJavaFile = createNonJavaFile();

        FileAnalyzerException exception = assertThrows(
                FileAnalyzerException.class,
                () -> validator.validate(nonJavaFile)
        );

        assertEquals("File must be a Java source file (.java)", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionForNonExistentFile() {
        Path nonExistentFile = tempDir.resolve("NonExistent.java");

        FileAnalyzerException exception = assertThrows(
                FileAnalyzerException.class,
                () -> validator.validate(nonExistentFile)
        );

        assertEquals("File does not exist: " + nonExistentFile, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionForDirectory() throws IOException {
        Path directory = Files.createDirectory(tempDir.resolve("testDir"));

        FileAnalyzerException exception = assertThrows(
                FileAnalyzerException.class,
                () -> validator.validate(directory)
        );

        assertEquals("File must be a Java source file (.java)", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionForUnreadableFile() throws IOException {
        Path javaFile = createValidJavaFile();
        File file = javaFile.toFile();
        file.setReadable(false);

        FileAnalyzerException exception = assertThrows(
                FileAnalyzerException.class,
                () -> validator.validate(javaFile)
        );

        assertEquals("File can't be read: " + javaFile, exception.getMessage());
    }

    private Path createValidJavaFile() throws IOException {
        Path javaFile = tempDir.resolve("TestClass.java");
        Files.writeString(javaFile, "public class TestClass {}");
        return javaFile;
    }

    private Path createNonJavaFile() throws IOException {
        Path txtFile = tempDir.resolve("test.txt");
        Files.writeString(txtFile, "This is not a Java file");
        return txtFile;
    }
}