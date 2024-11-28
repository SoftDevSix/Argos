package edu.usb.argos.astprocessor.reader;

import edu.usb.argos.astprocessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.usb.argos.astprocessor.reader.application.services.DirectoryAnalyzerByPath;
import edu.usb.argos.astprocessor.reader.infraestructure.utils.SourceTreeAnalyzer;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

class DirectoryAnalyzerByPathTest {

    @Mock
    private IFileAnalyzer<Path, Object> mockFileAnalyzer;

    @Mock
    private SourceTreeAnalyzer mockTreeAnalyzer;

    private DirectoryAnalyzerByPath<Object> directoryAnalyzer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        directoryAnalyzer = new DirectoryAnalyzerByPath<>(mockFileAnalyzer, mockTreeAnalyzer);
    }

    @Test
    void testAnalyzeDirectory_NoJavaFiles() throws FileAnalyzerException {
        Path mockPath = mock(Path.class);

        when(mockTreeAnalyzer.getJavaFiles(mockPath)).thenReturn(Collections.emptyList());

        List<Object> result = directoryAnalyzer.analyzeDirectory(mockPath);

        assertEquals(0, result.size());
        verify(mockFileAnalyzer, never()).readFile(any(Path.class));
    }

    @Test
    void testAnalyzeDirectory_EmptyDirectoryPath() throws FileAnalyzerException {
        Path mockPath = mock(Path.class);

        when(mockTreeAnalyzer.getJavaFiles(mockPath)).thenReturn(Collections.emptyList());

        List<Object> result = directoryAnalyzer.analyzeDirectory(mockPath);

        assertEquals(0, result.size());
        verify(mockFileAnalyzer, never()).readFile(any(Path.class));
    }
}
