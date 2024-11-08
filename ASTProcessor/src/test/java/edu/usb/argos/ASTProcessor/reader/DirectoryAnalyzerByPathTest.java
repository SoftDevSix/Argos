package edu.usb.argos.ASTProcessor.reader;

import edu.usb.argos.ASTProcessor.application.analyzers.IFileAnalyzer;
import edu.usb.argos.ASTProcessor.application.logging.IAppLogger;
import edu.usb.argos.ASTProcessor.infrastructure.analyzers.directoryAnalyzers.DirectoryAnalyzerByPath;
import edu.usb.argos.ASTProcessor.infrastructure.utils.SourceTreeAnalyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DirectoryAnalyzerByPathTest {

    @Mock
    private IFileAnalyzer<Path, Object> fileAnalyzer;
    @Mock
    private SourceTreeAnalyzer sourceTreeAnalyzer;
    @Mock
    private IAppLogger logger;

    @InjectMocks
    private DirectoryAnalyzerByPath<Object> directoryAnalyzer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAnalyzeDirectory_ReturnsASTList() throws Exception {
        Path testPath = Path.of("src/test/resources/project_example");
        List<Path> javaFiles = List.of(Path.of("SimpleClass.java"), Path.of("AnotherClass.java"));
        Object mockAst1 = new Object();
        Object mockAst2 = new Object();

        when(sourceTreeAnalyzer.getJavaFiles(testPath)).thenReturn(javaFiles);
        when(fileAnalyzer.readFile(javaFiles.get(0))).thenReturn(mockAst1);
        when(fileAnalyzer.readFile(javaFiles.get(1))).thenReturn(mockAst2);

        List<Object> result = directoryAnalyzer.analyzeDirectory(testPath);

        assertEquals(2, result.size());
        assertTrue(result.contains(mockAst1));
        assertTrue(result.contains(mockAst2));
        verify(sourceTreeAnalyzer, times(1)).getJavaFiles(testPath);
        verify(fileAnalyzer, times(1)).readFile(javaFiles.get(0));
        verify(fileAnalyzer, times(1)).readFile(javaFiles.get(1));
        verify(logger, never()).error(anyString(), any());
    }

    @Test
    public void testAnalyzeDirectory_NoJavaFiles() throws Exception {
        Path testPath = Path.of("src/test/resources/project_example");
        List<Path> javaFiles = List.of();

        when(sourceTreeAnalyzer.getJavaFiles(testPath)).thenReturn(javaFiles);

        List<Object> result = directoryAnalyzer.analyzeDirectory(testPath);

        assertTrue(result.isEmpty());
        verify(sourceTreeAnalyzer, times(1)).getJavaFiles(testPath);
        verify(fileAnalyzer, never()).readFile(any(Path.class));
        verify(logger, never()).error(anyString(), any());
    }
}
