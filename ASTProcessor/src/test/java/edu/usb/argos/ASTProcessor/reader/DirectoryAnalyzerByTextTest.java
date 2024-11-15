package edu.usb.argos.ASTProcessor.reader;

import edu.usb.argos.ASTProcessor.reader.application.services.DirectoryAnalyzerByText;
import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.domain.interfaces.IFileAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DirectoryAnalyzerByTextTest {

    @Mock
    private IFileAnalyzer<String, Object> mockFileAnalyzer;

    @InjectMocks
    private DirectoryAnalyzerByText<Object> directoryAnalyzer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        directoryAnalyzer = new DirectoryAnalyzerByText<>(mockFileAnalyzer);
    }

    @Test
    void testAnalyzeDirectory_AllFilesValid() throws FileAnalyzerException {
        String[] sourceCode = { "code1", "code2" };
        Object mockAst1 = new Object();
        Object mockAst2 = new Object();

        when(mockFileAnalyzer.readFile("code1")).thenReturn(mockAst1);
        when(mockFileAnalyzer.readFile("code2")).thenReturn(mockAst2);

        List<Object> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(2, result.size());
        assertEquals(mockAst1, result.get(0));
        assertEquals(mockAst2, result.get(1));
    }

    @Test
    void testAnalyzeDirectory_WithNullAst() throws FileAnalyzerException {
        String[] sourceCode = { "code1", "code2" };
        Object mockAst1 = new Object();

        when(mockFileAnalyzer.readFile("code1")).thenReturn(mockAst1);
        when(mockFileAnalyzer.readFile("code2")).thenReturn(null);

        List<Object> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(1, result.size());
        assertEquals(mockAst1, result.get(0));
    }

    @Test
    void testAnalyzeDirectory_WithFileAnalyzerException() throws FileAnalyzerException {
        String[] sourceCode = { "code1", "code2" };
        Object mockAst1 = new Object();

        when(mockFileAnalyzer.readFile("code1")).thenReturn(mockAst1);
        when(mockFileAnalyzer.readFile("code2")).thenThrow(new FileAnalyzerException("Test exception"));

        List<Object> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(1, result.size());
        assertEquals(mockAst1, result.get(0));
        verify(mockFileAnalyzer).readFile("code2");
    }

    @Test
    void testAnalyzeDirectory_EmptySourceCodeArray() throws FileAnalyzerException {
        String[] sourceCode = {};

        List<Object> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(0, result.size());
        verify(mockFileAnalyzer, never()).readFile(anyString());
    }

    @Test
    void testAnalyzeDirectory_NullSourceCodeArray() throws FileAnalyzerException {
        List<Object> result = directoryAnalyzer.analyzeDirectory(null);

        assertEquals(0, result.size());
        verify(mockFileAnalyzer, never()).readFile(anyString());
    }
}
