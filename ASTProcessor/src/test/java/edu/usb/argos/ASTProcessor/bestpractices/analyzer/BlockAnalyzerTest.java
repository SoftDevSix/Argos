package edu.usb.argos.ASTProcessor.bestpractices.analyzer;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedDetection;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockAnalyzerTest {
    @Test
    void testAnalyzeBlockDetectsHardcodedValuesInAssignments() {
        String javaSource = """
               public class TestClass {
                    public TestClass() {
                        int number = 42;
                        String text = "Hardcoded in Constructor";
                    }
                }
              \s""";

        CharStream charStream = CharStreams.fromString(javaSource);
        JavaLexer lexer = new JavaLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);
        JavaParser.ClassDeclarationContext classContext =
                parser.compilationUnit().typeDeclaration(0).classDeclaration();

        List<HardcodedDetection> detectedValues = new ArrayList<>();

        for (JavaParser.ClassBodyDeclarationContext member : classContext.classBody().classBodyDeclaration()) {
            JavaParser.ConstructorDeclarationContext constructor = member.memberDeclaration().constructorDeclaration();
            BlockAnalyzer.getInstance().analyze(constructor.block(), detectedValues);
        }

        assertEquals(2, detectedValues.size());
        assertEquals("42", detectedValues.get(0).getHardcodedValue());
        assertEquals(3, detectedValues.get(0).getLineNumber());
        assertEquals("\"Hardcoded in Constructor\"", detectedValues.get(1).getHardcodedValue());
        assertEquals(4, detectedValues.get(1).getLineNumber());
    }
}
