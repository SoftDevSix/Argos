package edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedValueMatcher;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection.AttributeDetectionStrategy;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection.ConstructorDetectionStrategy;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection.IDetectionStrategy;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection.MethodDetectionStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetectionsTest {

    private JavaParser.ClassDeclarationContext parseJavaSource(String javaSource) {
        CharStream charStream = CharStreams.fromString(javaSource);
        JavaLexer lexer = new JavaLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);
        return parser.compilationUnit().typeDeclaration(0).classDeclaration();
    }

    private List<HardcodedDetection> detectHardcodedValues(JavaParser.ClassDeclarationContext classCtx, IDetectionStrategy strategy) {
        List<HardcodedDetection> detectedValues = new ArrayList<>();
        for (JavaParser.ClassBodyDeclarationContext member : classCtx.classBody().classBodyDeclaration()) {
            strategy.detectHardcodedValues(member, HardcodedValueMatcher.getInstance(), detectedValues);
        }
        return detectedValues;
    }

    @Test
    void testDetectHardcodedValuesInAttributes() {
        String javaSource = """
                public class TestClass {
                    private String hardcoded = "Hardcoded Value";
                    private final String constant = "Ignored Value";
                    int number = 42;
                }
                """;

        JavaParser.ClassDeclarationContext classCtx = parseJavaSource(javaSource);
        List<HardcodedDetection> detectedValues = detectHardcodedValues(classCtx, new AttributeDetectionStrategy());

        assertEquals(2, detectedValues.size());
        assertEquals("\"Hardcoded Value\"", detectedValues.get(0).getHardcodedValue());
        assertEquals(2, detectedValues.get(0).getLineNumber());
        assertEquals("42", detectedValues.get(1).getHardcodedValue());
        assertEquals(4, detectedValues.get(1).getLineNumber());
    }

    @Test
    void testDetectHardcodedValuesInConstructors() {
        String javaSource = """
               public class TestClass {
                    public TestClass() {
                        int number = 42;
                        String text = "Hardcoded in Constructor";
                    }
                  \s
                    public TestClass(int number, String text) {
                        this.number = number;
                        this.text = text;
                        method();
                        method("Hardcoded argument");
                    }
                }
              \s""";

        JavaParser.ClassDeclarationContext classCtx = parseJavaSource(javaSource);
        List<HardcodedDetection> detectedValues = detectHardcodedValues(classCtx, new ConstructorDetectionStrategy());

        assertEquals(3, detectedValues.size());
        assertEquals("42", detectedValues.get(0).getHardcodedValue());
        assertEquals(3, detectedValues.get(0).getLineNumber());
        assertEquals("\"Hardcoded in Constructor\"", detectedValues.get(1).getHardcodedValue());
        assertEquals(4, detectedValues.get(1).getLineNumber());
        assertEquals("\"Hardcoded argument\"", detectedValues.get(2).getHardcodedValue());
        assertEquals(11, detectedValues.get(2).getLineNumber());
    }

    @Test
    void testDetectHardcodedValuesInMethods() {
        String javaSource = """
                public class TestClass {
                    public void testMethod() {
                        String message = "Hardcoded Message";
                        int number = 99;
                        testMethod(101);
                    }
                  \s
                    private int testMethod(int number) {
                        boolean indicator = true;
                        int sum = number + 2;
                        int sum1 = number++;
                        return sum + sum1;
                    }
                }
              \s""";

        JavaParser.ClassDeclarationContext classCtx = parseJavaSource(javaSource);
        List<HardcodedDetection> detectedValues = detectHardcodedValues(classCtx, new MethodDetectionStrategy());

        assertEquals(4, detectedValues.size());
        assertEquals("\"Hardcoded Message\"", detectedValues.get(0).getHardcodedValue());
        assertEquals(3, detectedValues.get(0).getLineNumber());

        assertEquals("99", detectedValues.get(1).getHardcodedValue());
        assertEquals(4, detectedValues.get(1).getLineNumber());

        assertEquals("101", detectedValues.get(2).getHardcodedValue());
        assertEquals(5, detectedValues.get(2).getLineNumber());

        assertEquals("true", detectedValues.get(3).getHardcodedValue());
        assertEquals(9, detectedValues.get(3).getLineNumber());
    }
}
