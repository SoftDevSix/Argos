package edu.usb.argos.ASTProcessor.DirectoryAnalyzers;

import edu.usb.argos.ASTProcessor.Validators.PathValidators.DirectoryPathAnalyzer;
import edu.usb.argos.ASTProcessor.Validators.PathValidators.IPathValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DirectoryPathValidatorTest {

    private Path tempDir;
    private IPathValidator pathValidator;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("testDirectory");
        pathValidator = new DirectoryPathAnalyzer();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testGetJavaFilesInNonExistentDirectory() {
        Path nonExistentPath = tempDir.resolve("nonexistentDir");

        assertThrows(NoSuchFileException.class, () -> {
            pathValidator.validatePath(nonExistentPath);
        });
    }

    @Test
    void testGetJavaFilesInNonDirectory() throws IOException {
        Path file = Files.createFile(tempDir.resolve("NotADirectory.java"));

        assertThrows(IllegalArgumentException.class, () -> {
            pathValidator.validatePath(file);
        });
    }
}
