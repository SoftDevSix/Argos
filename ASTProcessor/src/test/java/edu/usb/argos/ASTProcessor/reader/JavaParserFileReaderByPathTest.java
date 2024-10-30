package edu.usb.argos.ASTProcessor.reader;

import com.github.javaparser.ast.CompilationUnit;
import edu.usb.argos.ASTProcessor.reader.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.interfaces.IFileValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JavaParserFileReaderByPathTest {
    @Mock
    private IFileValidationStrategy<Path> mockValidationStrategy;

    private JavaParserFileReaderByPath fileReader;

    @TempDir
    private Path tempDir;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fileReader = new JavaParserFileReaderByPath(mockValidationStrategy);
    }

    @Test
    public void shouldSuccessfullyParseValidJavaFile() throws Exception {
        Path validFile = createValidJavaFile();
        doNothing().when(mockValidationStrategy).validate(any());

        CompilationUnit result = fileReader.readFile(validFile);

        assertNotNull(result);
        assertEquals("TestClass", result.getType(0).getName().asString());
        verify(mockValidationStrategy).validate(validFile);
    }

    @Test
    public void shouldThrowExceptionWhenValidationFails() throws Exception {
        Path invalidFile = tempDir.resolve("Invalid.java");
        doThrow(new FileAnalyzerException("Validation failed"))
                .when(mockValidationStrategy).validate(any());

        assertThrows(FileAnalyzerException.class, () -> fileReader.readFile(invalidFile));
        verify(mockValidationStrategy).validate(invalidFile);
    }

    @Test
    public void shouldThrowExceptionForInvalidJavaCode() throws Exception {
        Path invalidFile = createInvalidJavaFile();
        doNothing().when(mockValidationStrategy).validate(any());

        assertThrows(FileAnalyzerException.class, () -> fileReader.readFile(invalidFile));
        verify(mockValidationStrategy).validate(invalidFile);
    }

    @Test
    public void shouldThrowExceptionWhenIOExceptionOccurs() throws Exception {
        Path nonReadableFile = createValidJavaFile();
        nonReadableFile.toFile().setReadable(false);
        doNothing().when(mockValidationStrategy).validate(any());

        assertThrows(FileAnalyzerException.class, () -> fileReader.readFile(nonReadableFile));
        verify(mockValidationStrategy).validate(nonReadableFile);
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

    private Path createInvalidJavaFile() throws IOException {
        Path invalidFile = tempDir.resolve("Invalid.java");
        Files.writeString(invalidFile, "This is not valid Java code");
        return invalidFile;
    }
}
