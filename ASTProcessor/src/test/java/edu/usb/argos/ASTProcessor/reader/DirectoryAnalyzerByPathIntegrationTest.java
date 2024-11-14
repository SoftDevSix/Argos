package edu.usb.argos.ASTProcessor.reader;

import edu.usb.argos.ASTProcessor.reader.application.services.DirectoryAnalyzerByPath;
import edu.usb.argos.ASTProcessor.reader.application.services.FileReaderByPath;
import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.domain.interfaces.IFileValidationStrategy;
import edu.usb.argos.ASTProcessor.reader.infraestructure.utils.SourceTreeAnalyzer;
import edu.usb.argos.ASTProcessor.reader.infraestructure.validation.DirectoryPathValidator;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DirectoryAnalyzerByPathIntegrationTest {

    @Mock
    private IFileValidationStrategy<Path> validationStrategy;

    @InjectMocks
    private FileReaderByPath fileReader;

    private DirectoryAnalyzerByPath<ParseTree> directoryAnalyzer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileReader = new FileReaderByPath(validationStrategy);
        directoryAnalyzer = new DirectoryAnalyzerByPath<>(fileReader, new SourceTreeAnalyzer(new DirectoryPathValidator()));
    }

    @Test
    void testAnalyzeDirectory_WithValidJavaFilesInSubdirectories() throws Exception {
        Path subDir1 = Files.createDirectory(tempDir.resolve("subDir1"));
        Path subDir2 = Files.createDirectory(tempDir.resolve("subDir1/subDir2"));

        createJavaFile(tempDir, "Test1.java", "public class Test1 {}");
        createJavaFile(subDir1, "Test2.java", "public class Test2 { void method() {} }");
        createJavaFile(subDir2, "Test3.java", "public class Test3 { int x = 10; }");

        doNothing().when(validationStrategy).validate(any());

        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(tempDir);

        assertEquals(3, result.size());
        result.forEach(Assertions::assertNotNull);
        verify(validationStrategy, times(3)).validate(any());
    }

    @Test
    void testAnalyzeDirectory_EmptyDirectory() throws Exception {
        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(tempDir);

        assertTrue(result.isEmpty());
        verify(validationStrategy, never()).validate(any());
    }

    @Test
    void testAnalyzeDirectory_WithNonJavaFilesInSubdirectories() throws Exception {
        Path subDir = Files.createDirectory(tempDir.resolve("subDir"));
        Path textFile = subDir.resolve("notJavaFile.txt");
        Files.writeString(textFile, "This is not a Java file.");

        doNothing().when(validationStrategy).validate(any());

        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(tempDir);

        assertTrue(result.isEmpty());
        verify(validationStrategy, never()).validate(any());
    }

    @Test
    void testAnalyzeDirectory_WithInvalidJavaFileInSubdirectory() throws Exception {
        Path subDir = Files.createDirectory(tempDir.resolve("subDir"));
        Path invalidJavaFile = createJavaFile(subDir, "Invalid.java", "public class Invalid { syntax error here");

        doThrow(new FileAnalyzerException("Validation failed")).when(validationStrategy).validate(any());

        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(tempDir);

        assertTrue(result.isEmpty());
        verify(validationStrategy).validate(invalidJavaFile);
    }

    private Path createJavaFile(Path dir, String fileName, String content) throws Exception {
        Path javaFile = dir.resolve(fileName);
        Files.writeString(javaFile, content);
        return javaFile;
    }
}
