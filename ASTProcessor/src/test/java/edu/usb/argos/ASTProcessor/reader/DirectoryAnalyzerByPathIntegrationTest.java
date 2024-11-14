package edu.usb.argos.ASTProcessor.reader;
import edu.usb.argos.ASTProcessor.reader.application.services.DirectoryAnalyzerByPath;
import edu.usb.argos.ASTProcessor.reader.application.services.FileReaderByPath;
import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.domain.interfaces.IFileValidationStrategy;
import edu.usb.argos.ASTProcessor.reader.infraestructure.utils.SourceTreeAnalyzer;
import edu.usb.argos.ASTProcessor.reader.infraestructure.validation.DirectoryPathValidator;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;


class DirectoryAnalyzerByPathIntegrationTest {

    @Mock
    private IFileValidationStrategy<Path> validationStrategy;

    @InjectMocks
    private FileReaderByPath fileReader;

    private DirectoryAnalyzerByPath<ParseTree> directoryAnalyzer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileReader = new FileReaderByPath(validationStrategy);
        directoryAnalyzer = new DirectoryAnalyzerByPath<>(fileReader, new SourceTreeAnalyzer(new DirectoryPathValidator()));
    }

    @Test
    void testAnalyzeValidJavaFiles() throws Exception {
        Path validFilesDir = Paths.get("src/test/resources/java_files/valid");

        doNothing().when(validationStrategy).validate(any());

        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(validFilesDir);

        assertEquals(5, result.size());
        result.forEach(Assertions::assertNotNull);
        verify(validationStrategy, times(5)).validate(any());
    }

    @Test
    void testAnalyzeInvalidJavaFiles() throws Exception {
        Path invalidFilesDir = Paths.get("src/test/resources/java_files/invalid");

        doThrow(new FileAnalyzerException("Validation failed")).when(validationStrategy).validate(any());

        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(invalidFilesDir);

        assertTrue(result.isEmpty());
        verify(validationStrategy, times(2)).validate(any());
    }
}
