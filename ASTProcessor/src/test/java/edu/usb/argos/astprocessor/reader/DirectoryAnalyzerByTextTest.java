package edu.usb.argos.astprocessor.reader;

import edu.usb.argos.astprocessor.reader.application.services.DirectoryAnalyzerByText;
import edu.usb.argos.astprocessor.reader.domain.exceptions.ASTAnalysisException;
import edu.usb.argos.astprocessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileAnalyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void testAnalyzeDirectoryAllFilesValid() throws FileAnalyzerException {
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
    void testAnalyzeDirectoryWithNullAst() throws FileAnalyzerException {
        String[] sourceCode = {"code1", "code2"};
        Optional<Object> mockAst1 = Optional.of(new Object());

        when(mockFileAnalyzer.readFile("code1")).thenReturn(mockAst1);
        when(mockFileAnalyzer.readFile("code2")).thenReturn(Optional.empty());

        List<Object> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(1, result.size());
        assertEquals(mockAst1.get(), result.get(0));
    }

    @Test
    void testAnalyzeDirectoryWithFileAnalyzerException() throws FileAnalyzerException {
        String[] sourceCode = {"code1", "code2"};
        Optional<Object> mockAst1 = Optional.of(new Object());

        when(mockFileAnalyzer.readFile("code1")).thenReturn(mockAst1);
        when(mockFileAnalyzer.readFile("code2")).thenThrow(new FileAnalyzerException("Test exception"));

        assertThrowsExactly(ASTAnalysisException.class, () -> directoryAnalyzer.analyzeDirectory(sourceCode));
    }

    @Test
    void testAnalyzeDirectoryEmptySourceCodeArray() throws FileAnalyzerException {
        String[] sourceCode = {};

        List<Object> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(0, result.size());
        verify(mockFileAnalyzer, never()).readFile(anyString());
    }
}
