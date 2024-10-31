package edu.usb.argos.ASTProcessor.directory_analyzers;

import edu.usb.argos.ASTProcessor.reader.directory_readers.SourceTreeAnalyzer;
import edu.usb.argos.ASTProcessor.reader.validations.DirectoryPathAnalyzer;
import edu.usb.argos.ASTProcessor.reader.interfaces.IPathValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SourceTreeAnalyzerTest {

    private Path tempDir;
    private SourceTreeAnalyzer analyzer;

    @BeforeEach
    void setUp() throws IOException {
        IPathValidator pathValidator = new DirectoryPathAnalyzer();
        tempDir = Files.createTempDirectory("testDirectory");
        analyzer = new SourceTreeAnalyzer(pathValidator);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testGetJavaFilesInValidDirectory() throws Exception {
        Path javaFile = Files.createFile(tempDir.resolve("TestFile.java"));
        List<Path> javaFiles = analyzer.getJavaFiles(tempDir);

        assertEquals(1, javaFiles.size());
        assertTrue(javaFiles.contains(javaFile));
    }

    @Test
    void testGetJavaFilesInNonJavaFilesDirectory() throws Exception {
        Files.createFile(tempDir.resolve("TestFile.txt"));
        List<Path> javaFiles = analyzer.getJavaFiles(tempDir);

        assertEquals(0, javaFiles.size());
    }
}
