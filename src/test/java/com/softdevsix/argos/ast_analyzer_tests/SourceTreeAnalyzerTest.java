package com.softdevsix.argos.ast_analyzer_tests;

import com.softdevsix.argos.ast_analyzer.SourceTreeAnalyzer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SourceTreeAnalyzerTest {

    private Path tempDir;
    private SourceTreeAnalyzer analyzer;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("testDirectory");
        analyzer = new SourceTreeAnalyzer();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(file -> {
                    if (file.isDirectory()) {
                        file.delete();
                    } else {
                        file.delete();
                    }
                });
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

    @Test
    void testGetJavaFilesInNonExistentDirectory() {
        Path nonExistentPath = tempDir.resolve("nonexistentDir");

        assertThrows(NoSuchFileException.class, () -> {
            analyzer.getJavaFiles(nonExistentPath);
        });
    }

    @Test
    void testGetJavaFilesInNonDirectory() throws IOException {
        Path file = Files.createFile(tempDir.resolve("NotADirectory.java"));

        assertThrows(IllegalArgumentException.class, () -> {
            analyzer.getJavaFiles(file);
        });
    }
}
