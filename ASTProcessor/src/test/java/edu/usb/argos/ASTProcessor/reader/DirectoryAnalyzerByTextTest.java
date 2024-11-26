package edu.usb.argos.ASTProcessor.reader;

import edu.usb.argos.ASTProcessor.reader.application.services.DirectoryAnalyzerByText;
import edu.usb.argos.ASTProcessor.reader.domain.exceptions.ASTAnalysisException;
import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.domain.interfaces.IFileAnalyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

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
        String[] sourceCode = {"code1", "code2"};
        Optional<Object> mockAst1 = Optional.of(new Object());
        Optional<Object> mockAst2 = Optional.of(new Object());

        when(mockFileAnalyzer.readFile("code1")).thenReturn(mockAst1);
        when(mockFileAnalyzer.readFile("code2")).thenReturn(mockAst2);

        List<Object> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(2, result.size());
        assertEquals(mockAst1.get(), result.get(0));
        assertEquals(mockAst2.get(), result.get(1));
    }

    @Test
    void testAnalyzeDirectory_WithNullAst() throws FileAnalyzerException {
        String[] sourceCode = {"code1", "code2"};
        Optional<Object> mockAst1 = Optional.of(new Object());

        when(mockFileAnalyzer.readFile("code1")).thenReturn(mockAst1);
        when(mockFileAnalyzer.readFile("code2")).thenReturn(Optional.empty());

        List<Object> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(1, result.size());
        assertEquals(mockAst1.get(), result.get(0));
    }

    @Test
    void testAnalyzeDirectory_WithFileAnalyzerException() throws FileAnalyzerException {
        String[] sourceCode = {"code1", "code2"};
        Optional<Object> mockAst1 = Optional.of(new Object());

        when(mockFileAnalyzer.readFile("code1")).thenReturn(mockAst1);
        when(mockFileAnalyzer.readFile("code2")).thenThrow(new FileAnalyzerException("Test exception"));

        assertThrowsExactly(ASTAnalysisException.class, () -> directoryAnalyzer.analyzeDirectory(sourceCode));
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
