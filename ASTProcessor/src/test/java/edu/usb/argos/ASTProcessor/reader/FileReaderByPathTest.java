package edu.usb.argos.astprocessor.reader;

import edu.usb.argos.astprocessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.astprocessor.reader.application.services.FileReaderByPath;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileValidationStrategy;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class FileReaderByPathTest {
    @Mock
    private IFileValidationStrategy<Path> mockValidationStrategy;
    private FileReaderByPath fileReader;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileReader = new FileReaderByPath(mockValidationStrategy);
    }

    @Test
    void shouldSuccessfullyParseValidJavaFile() throws Exception {
        Path validFile = createValidJavaFile();
        doNothing().when(mockValidationStrategy).validate(any());

        Optional<ParseTree> result = fileReader.readFile(validFile);

        assertTrue(result.isPresent());
        assertTrue(result.get().getText().contains("TestClass"));
        verify(mockValidationStrategy).validate(validFile);
    }

    @Test
    void shouldThrowExceptionWhenValidationFails() throws Exception {
        Path invalidFile = tempDir.resolve("Invalid.java");
        doThrow(new FileAnalyzerException("Validation failed"))
                .when(mockValidationStrategy).validate(any());

        assertThrows(FileAnalyzerException.class, () -> fileReader.readFile(invalidFile));
        verify(mockValidationStrategy).validate(invalidFile);
    }

    @Test
    void shouldThrowExceptionWhenIOExceptionOccurs() throws Exception {
        Path nonReadableFile = createValidJavaFile();
        nonReadableFile.toFile().setReadable(false);
        doNothing().when(mockValidationStrategy).validate(any());

        assertThrows(FileAnalyzerException.class, () -> fileReader.readFile(nonReadableFile));
        verify(mockValidationStrategy).validate(nonReadableFile);
    }

    @Test
    void shouldHandleComplexJavaFile() throws Exception {
        Path complexFile = createComplexJavaFile();
        doNothing().when(mockValidationStrategy).validate(any());

        Optional<ParseTree> result = fileReader.readFile(complexFile);

        assertTrue(result.isPresent());
        assertTrue(result.get().getText().contains("ComplexClass"));
        verify(mockValidationStrategy).validate(complexFile);
    }

    private Path createValidJavaFile() throws IOException {
        Path javaFile = tempDir.resolve("TestClass.java");
        String content =
                "public class TestClass {\n" +
                        "    public void testMethod() {\n" +
                        "        System.out.println(\"Hello World\");\n" +
                        "    }\n" +
                        "}";
        Files.writeString(javaFile, content);
        return javaFile;
    }

    private Path createComplexJavaFile() throws IOException {
        Path complexFile = tempDir.resolve("ComplexClass.java");
        String content =
                "package com.example;\n\n" +
                        "import java.util.List;\n" +
                        "import java.util.ArrayList;\n\n" +
                        "public class ComplexClass {\n" +
                        "    private List<String> items;\n\n" +
                        "    public ComplexClass() {\n" +
                        "        this.items = new ArrayList<>();\n" +
                        "    }\n\n" +
                        "    public void addItem(String item) {\n" +
                        "        items.add(item);\n" +
                        "    }\n\n" +
                        "    public List<String> getItems() {\n" +
                        "        return new ArrayList<>(items);\n" +
                        "    }\n" +
                        "}";
        Files.writeString(complexFile, content);
        return complexFile;
    }
}
