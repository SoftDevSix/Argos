package edu.usb.argos.ASTProcessor;

import com.github.javaparser.ast.CompilationUnit;

import main.java.edu.usb.argos.ASTProcessor.FileReader.FileAnalyzerException;
import main.java.edu.usb.argos.ASTProcessor.FileReader.FileAnalyzerValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class JavaParserFileReaderByTextTest {

    private JavaParserFileReaderByText javaParserFileReaderByText;
    private FileAnalyzerValidator fileAnalyzerValidator;

    @BeforeEach
    public void setup() {
        javaParserFileReaderByText = new JavaParserFileReaderByText();
        fileAnalyzerValidator = new FileAnalyzerValidator();
    }

    @Test
    public void testReadValidCode() throws Exception {
        String code = "class Test { void method() {} }";
        CompilationUnit result = javaParserFileReaderByText.read(code);
        assertNotNull(result);
        assertTrue(result.toString().contains("Test"));
    }

    @Test
    public void testReadFileWihoutTemplate() throws Exception {
        String emptyCode = "}{}";
        Exception exception = assertThrows(FileAnalyzerException.class, () -> {
            javaParserFileReaderByText.read(emptyCode);
        });
        assertEquals("Error to analyze de code", exception.getMessage());
    }

    @Test
    public void testReadCodeWithSyntaxError() throws Exception {
        String invalidCode = "class { void method() {} }";
        Exception exception = assertThrows(FileAnalyzerException.class, () -> {
            javaParserFileReaderByText.read(invalidCode);
        });
        assertEquals("Error to analyze de code", exception.getMessage());
    }

    @Test
    public void testReadMinimalClassDefinition() throws Exception {
        String minimalClass = "class EmptyClass {}";
        CompilationUnit result = javaParserFileReaderByText.read(minimalClass);
        assertNotNull(result);
        assertTrue(result.toString().contains("EmptyClass"));
    }

    @Test
    public void testReadMultipleClasses() throws Exception {
        String multipleClasses = "class Test1 { } class Test2 { void method() {} }";
        CompilationUnit result = javaParserFileReaderByText.read(multipleClasses);
        assertNotNull(result);
        assertTrue(result.toString().contains("Test1"));
        assertTrue(result.toString().contains("Test2"));
    }

    @Test
    public void testValidateInputWithEmptyText() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileAnalyzerValidator.validateInput("");
        });
        assertTrue(exception.getMessage().contains("Input text cannot be null or empty."));
    }

    @Test
    public void testValidateInputWithNullText() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileAnalyzerValidator.validateInput(null);
        });
        assertTrue(exception.getMessage().contains("Input text cannot be null or empty."));
    }

    @Test
    public void testValidateClassDeclarationWithClassPresent() throws Exception {
        String codeWithClass = "class SampleClass {}";
        CompilationUnit compilationUnit = javaParserFileReaderByText.read(codeWithClass);
        assertTrue(fileAnalyzerValidator.validateClassDeclaration(compilationUnit));
    }

    @Test
    public void testValidateClassDeclarationWithoutClass() throws Exception {
        String codeWithoutClass = "";
        CompilationUnit compilationUnit = javaParserFileReaderByText.read(codeWithoutClass);
        assertFalse(fileAnalyzerValidator.validateClassDeclaration(compilationUnit));
    }

    @Test
    public void testValidateJavaCodeByHeaderWithClassKeyword() throws Exception {
        String text = "class MyClass {}";
        assertTrue(fileAnalyzerValidator.validateJavaCodeByHeader(text));
    }

    @Test
    public void tesValidateJavaCodeByHeaderWithInterfaceKeyword() throws Exception {
        String text = "interface Interface {}";
        assertTrue(fileAnalyzerValidator.validateJavaCodeByHeader(text));
    }

    @Test
    public void testValidateJavaCodeByHeaderWithEnumKeyword() throws Exception {
        String code = "enum Enum {}";
        assertTrue(fileAnalyzerValidator.validateJavaCodeByHeader(code));
    }

    @Test
    public void testValidateJavaCodeByHeaderWithoutKeywords() throws Exception {
        String code = "public void someMethod() {}";
        assertFalse(fileAnalyzerValidator.validateJavaCodeByHeader(code));
    }
}