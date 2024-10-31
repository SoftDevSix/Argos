package edu.usb.argos.ASTProcessor.directory_analyzers;

import edu.usb.argos.ASTProcessor.reader.directory_readers.DirectoryAnalyzerByPath;
import edu.usb.argos.ASTProcessor.reader.directory_readers.SourceTreeAnalyzer;
import edu.usb.argos.ASTProcessor.reader.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.interfaces.IFileAnalyzer;
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

    @InjectMocks
    private DirectoryAnalyzerByPath<Object> directoryAnalyzer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        directoryAnalyzer = new DirectoryAnalyzerByPath<>(fileAnalyzer, sourceTreeAnalyzer);
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
    }

    @Test
    public void testAnalyzeDirectory_SourceTreeAnalyzerThrowsException() throws Exception {
        Path testPath = Path.of("src/test/resources/project_example");
        when(sourceTreeAnalyzer.getJavaFiles(testPath)).thenThrow(new Exception("Directory not accessible"));

        List<Object> result = directoryAnalyzer.analyzeDirectory(testPath);

        assertTrue(result.isEmpty());
        verify(sourceTreeAnalyzer, times(1)).getJavaFiles(testPath);
        verify(fileAnalyzer, never()).readFile(any(Path.class));
    }

    @Test
    public void testAnalyzeDirectory_FileAnalyzerThrowsFileAnalyzerException() throws Exception {
        Path testPath = Path.of("src/test/resources/project_example");
        List<Path> javaFiles = List.of(Path.of("SimpleClass.java"), Path.of("AnotherClass.java"));
        Object mockAst = new Object();

        when(sourceTreeAnalyzer.getJavaFiles(testPath)).thenReturn(javaFiles);
        when(fileAnalyzer.readFile(javaFiles.get(0))).thenReturn(mockAst);
        when(fileAnalyzer.readFile(javaFiles.get(1))).thenThrow(new FileAnalyzerException("Error parsing file"));

        List<Object> result = directoryAnalyzer.analyzeDirectory(testPath);

        assertEquals(1, result.size());
        assertTrue(result.contains(mockAst));
        verify(sourceTreeAnalyzer, times(1)).getJavaFiles(testPath);
        verify(fileAnalyzer, times(1)).readFile(javaFiles.get(0));
        verify(fileAnalyzer, times(1)).readFile(javaFiles.get(1));
    }
}
