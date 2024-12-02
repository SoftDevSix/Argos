package edu.usb.argos.astprocessor.utils;

import edu.usb.argos.astprocessor.reader.infraestructure.utils.SourceTreeAnalyzer;
import edu.usb.argos.astprocessor.reader.infraestructure.validation.DirectoryPathValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SourceTreeAnalyzerTest {

    private Path tempDir;
    private SourceTreeAnalyzer analyzer;
    private DirectoryPathValidator mockPathValidator;

    @BeforeEach
    void setUp() throws IOException {
        mockPathValidator = mock(DirectoryPathValidator.class);
        tempDir = Files.createTempDirectory("testDirectory");
        analyzer = new SourceTreeAnalyzer(mockPathValidator);
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
    void testGetJavaFilesInNestedDirectories() throws Exception {
        Path subDir1 = Files.createDirectory(tempDir.resolve("subDir1"));
        Path subDir2 = Files.createDirectory(tempDir.resolve("subDir1/subDir2"));
        Path subDir3 = Files.createDirectory(tempDir.resolve("subDir3"));

        Path javaFile1 = Files.createFile(tempDir.resolve("Main.java"));
        Path javaFile2 = Files.createFile(subDir1.resolve("SubFile1.java"));
        Path javaFile3 = Files.createFile(subDir2.resolve("SubFile2.java"));
        Path textFile = Files.createFile(subDir3.resolve("TextFile.txt"));

        List<Path> javaFiles = analyzer.getJavaFiles(tempDir);
        assertEquals(3, javaFiles.size());
        assertTrue(javaFiles.contains(javaFile1));
        assertTrue(javaFiles.contains(javaFile2));
        assertTrue(javaFiles.contains(javaFile3));
        assertFalse(javaFiles.contains(textFile));
    }

    @Test
    void testGetJavaFilesInNonJavaFilesDirectory() throws Exception {
        Files.createFile(tempDir.resolve("TestFile.txt"));
        List<Path> javaFiles = analyzer.getJavaFiles(tempDir);

        assertEquals(0, javaFiles.size());
    }
}