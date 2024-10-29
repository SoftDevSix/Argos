package edu.usb.argos.ASTProcessor;

import com.github.javaparser.ast.CompilationUnit;
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

    @BeforeEach
    public void setup() {
        javaParserFileReaderByText = new JavaParserFileReaderByText();
    }

    @Test
    public void testReadValidCode() {
        String code = "class Test { void method() {} }";
        CompilationUnit result = javaParserFileReaderByText.read(code);
        assertNotNull(result);
        assertTrue(result.toString().contains("Test"));
    }

    @Test
    public void testReadEmptyCode() {
        String emptyCode = "{}";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            javaParserFileReaderByText.read(emptyCode);
        });
        assertEquals("Error to analyze de code", exception.getMessage());
    }

    @Test
    public void testReadCodeWithSyntaxError() {
        String invalidCode = "class { void method() {} }";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            javaParserFileReaderByText.read(invalidCode);
        });
        assertEquals("Error to analyze de code", exception.getMessage());
    }

    @Test
    public void testReadMinimalClassDefinition() {
        String minimalClass = "class EmptyClass {}";
        CompilationUnit result = javaParserFileReaderByText.read(minimalClass);
        assertNotNull(result);
        assertTrue(result.toString().contains("EmptyClass"));
    }

    @Test
    public void testReadMultipleClasses() {
        String multipleClasses = "class Test1 { } class Test2 { void method() {} }";
        CompilationUnit result = javaParserFileReaderByText.read(multipleClasses);
        assertNotNull(result);
        assertTrue(result.toString().contains("Test1"));
        assertTrue(result.toString().contains("Test2"));
    }

    @Test
    public void testValidateInputWithEmptyText() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            javaParserFileReaderByText.validateInput("");
        });
        assertTrue(exception.getMessage().contains("Input text cannot be null or empty."));
    }

    @Test
    public void testValidateInputWithNullText() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            javaParserFileReaderByText.validateInput(null);
        });
        assertTrue(exception.getMessage().contains("Input text cannot be null or empty."));
    }

    @Test
    public void testIsClassDeclarationWithClassPresent() {
        String codeWithClass = "class SampleClass {}";
        CompilationUnit compilationUnit = javaParserFileReaderByText.read(codeWithClass);
        assertTrue(javaParserFileReaderByText.isClassDeclaration(compilationUnit));
    }

    @Test
    public void testIsClassDeclarationWithoutClass() {
        String codeWithoutClass = "";
        CompilationUnit compilationUnit = javaParserFileReaderByText.read(codeWithoutClass);
        assertFalse(javaParserFileReaderByText.isClassDeclaration(compilationUnit));
    }

    @Test
    public void testIsValidJavaCodeByHeaderWithClassKeyword() {
        String text = "class MyClass {}";
        assertTrue(javaParserFileReaderByText.isValidJavaCodeByHeader(text));
    }

    @Test
    public void tesIsValidJavaCodeByHeaderWithInterfaceKeyword() {
        String text = "interface Interface {}";
        assertTrue(javaParserFileReaderByText.isValidJavaCodeByHeader(text));
    }

    @Test
    public void testIsValidJavaCodeByHeaderWithEnumKeyword() {
        String code = "enum Enum {}";
        assertTrue(javaParserFileReaderByText.isValidJavaCodeByHeader(code));
    }

    @Test
    public void testIsValidJavaCodeByHeaderWithoutKeywords() {
        String code = "public void someMethod() {}";
        assertFalse(javaParserFileReaderByText.isValidJavaCodeByHeader(code));
    }
}