package edu.usb.argos.ASTProcessor.lexer;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LexerDirectoryProcessorTest {
    private LexerDirectoryProcessor directoryProcessor;
    private Path tempDirectory;
    private Path testFile1;
    private Path testFile2;

    @BeforeEach
    void setUp() throws IOException {
        directoryProcessor = new LexerDirectoryProcessor();
        tempDirectory = Files.createTempDirectory("testDir");

        testFile1 = tempDirectory.resolve("TestFile1.java");
        Files.writeString(testFile1, "public class Test1 {}");

        testFile2 = tempDirectory.resolve("TestFile2.java");
        Files.writeString(testFile2, "int x = 10;");

        Path nonJavaFile = tempDirectory.resolve("NonJavaFile.txt");
        Files.writeString(nonJavaFile, "This should be ignored.");
    }

    @Test
    void testGetTokensFromDirectoryByFile() throws IOException {
        Map<String, CommonTokenStream> tokensByFile = directoryProcessor.getTokensFromDirectoryByFile(tempDirectory.toString());

        assertEquals(2, tokensByFile.size());
        assertTrue(tokensByFile.keySet().stream().allMatch(path -> path.endsWith(".java")));

        List<Integer> expectedTypesFile1 = List.of(
                JavaLexer.PUBLIC, JavaLexer.CLASS, JavaLexer.IDENTIFIER,
                JavaLexer.LBRACE, JavaLexer.RBRACE, JavaLexer.EOF
        );
        List<Integer> actualTypesFile1 = tokensByFile.get(testFile1.toString()).getTokens().stream().map(Token::getType).toList();
        assertEquals(expectedTypesFile1, actualTypesFile1);

        List<Integer> expectedTypesFile2 = List.of(
                JavaLexer.INT, JavaLexer.IDENTIFIER, JavaLexer.ASSIGN,
                JavaLexer.DECIMAL_LITERAL, JavaLexer.SEMI, JavaLexer.EOF
        );
        List<Integer> actualTypesFile2 = tokensByFile.get(testFile2.toString()).getTokens().stream().map(Token::getType).toList();
        assertEquals(expectedTypesFile2, actualTypesFile2);
    }

    @Test
    void testNoJavaFilesInDirectory() throws IOException {
        Path emptyDir = Files.createTempDirectory("emptyTestDir");
        Files.writeString(emptyDir.resolve("NotJava.txt"), "This is not a Java file.");

        Map<String, CommonTokenStream> tokensByFile = directoryProcessor.getTokensFromDirectoryByFile(emptyDir.toString());

        assertTrue(tokensByFile.isEmpty());
    }

    @Test
    void testJavaFilesWithEmptyContent() throws IOException {
        Path emptyJavaFile = tempDirectory.resolve("EmptyFile.java");
        Files.writeString(emptyJavaFile, "");

        Map<String, CommonTokenStream> tokensByFile = directoryProcessor.getTokensFromDirectoryByFile(tempDirectory.toString());

        assertTrue(tokensByFile.containsKey(emptyJavaFile.toString()));
        List<Token> tokens = tokensByFile.get(emptyJavaFile.toString()).getTokens();
        assertEquals(1, tokens.size());
        assertEquals(JavaLexer.EOF, tokens.get(0).getType());
    }

    @Test
    void testJavaFilesWithComplexCode() throws IOException {
        Path complexJavaFile = tempDirectory.resolve("ComplexFile.java");
        Files.writeString(complexJavaFile, "public class Complex { void method() {} int y = 20; }");

        Map<String, CommonTokenStream> tokensByFile = directoryProcessor.getTokensFromDirectoryByFile(tempDirectory.toString());

        assertTrue(tokensByFile.containsKey(complexJavaFile.toString()));

        List<Integer> expectedTypes = List.of(
                JavaLexer.PUBLIC, JavaLexer.CLASS, JavaLexer.IDENTIFIER,
                JavaLexer.LBRACE, JavaLexer.VOID, JavaLexer.IDENTIFIER,
                JavaLexer.LPAREN, JavaLexer.RPAREN, JavaLexer.LBRACE,
                JavaLexer.RBRACE, JavaLexer.INT, JavaLexer.IDENTIFIER,
                JavaLexer.ASSIGN, JavaLexer.DECIMAL_LITERAL,
                JavaLexer.SEMI, JavaLexer.RBRACE, JavaLexer.EOF
        );

        List<Integer> actualTypes = tokensByFile.get(complexJavaFile.toString()).getTokens().stream().map(Token::getType).toList();
        assertEquals(expectedTypes, actualTypes);
    }

    @Test
    void testCorrectFileNamesInTokensByFileMap() throws IOException {
        Map<String, CommonTokenStream> tokensByFile = directoryProcessor.getTokensFromDirectoryByFile(tempDirectory.toString());

        assertTrue(tokensByFile.containsKey(testFile1.toString()));
        assertTrue(tokensByFile.containsKey(testFile2.toString()));

        tokensByFile.keySet().forEach(path -> assertTrue(path.endsWith(".java")));
    }
}
