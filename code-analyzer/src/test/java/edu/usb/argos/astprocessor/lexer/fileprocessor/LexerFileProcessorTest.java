package edu.usb.argos.astprocessor.lexer.fileprocessor;

import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import edu.usb.argos.astprocessor.antlr.JavaLexer;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerFileProcessorTest {
    private List<Token> getTokensFromFile(String input) throws IOException {
        IFileProcessor<CommonTokenStream> processor = new LexerFileProcessor();

        Path tempFile = Files.createTempFile("testFile", ".java");
        Files.writeString(tempFile, input);

        List<Token> tokens = processor.getTokensFromFile(tempFile.toString()).getTokens();
        Files.delete(tempFile);
        return tokens;
    }

    private void assertTokenTypes(String input, List<Integer> expectedTypes) throws IOException {
        List<Token> tokens = getTokensFromFile(input);
        List<Integer> actualTypes = new ArrayList<>(tokens.stream().map(Token::getType).toList());
        assertEquals(expectedTypes, actualTypes);
    }

    @Test
    void testGetTokensFromFile() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.IDENTIFIER, JavaLexer.DECIMAL_LITERAL,
                JavaLexer.ADD, JavaLexer.SUB, JavaLexer.MUL,
                JavaLexer.DIV, Token.EOF
        );
        assertTokenTypes("abc 123 + - * /", expectedTypes);
    }

    @Test
    void testGetTokensFromFile_WithConditionalAndOperators() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.IF, JavaLexer.LPAREN, JavaLexer.IDENTIFIER,
                JavaLexer.LT, JavaLexer.IDENTIFIER,
                JavaLexer.RPAREN, JavaLexer.LBRACE, JavaLexer.IDENTIFIER,
                JavaLexer.ADD_ASSIGN, JavaLexer.DECIMAL_LITERAL,
                JavaLexer.SEMI, JavaLexer.RBRACE, JavaLexer.EOF
        );
        assertTokenTypes("if (a < b) { a += 2; }", expectedTypes);
    }

    @Test
    void testGetTokensFromFile_WithClassAndMethodStructure() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.PUBLIC, JavaLexer.CLASS, JavaLexer.IDENTIFIER,
                JavaLexer.LBRACE, JavaLexer.VOID, JavaLexer.IDENTIFIER,
                JavaLexer.LPAREN, JavaLexer.INT, JavaLexer.IDENTIFIER,
                JavaLexer.RPAREN, JavaLexer.LBRACE, JavaLexer.IDENTIFIER,
                JavaLexer.INC, JavaLexer.SEMI, JavaLexer.RBRACE,
                JavaLexer.RBRACE, JavaLexer.EOF
        );
        assertTokenTypes("public class MyClass { void doSomething(int a) { a++; } }", expectedTypes);
    }

    @Test
    void testGetTokensFromFile_WithLiteralsAndOperators() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.BOOLEAN, JavaLexer.IDENTIFIER, JavaLexer.ASSIGN,
                JavaLexer.BOOL_LITERAL, JavaLexer.SEMI, JavaLexer.INT, JavaLexer.IDENTIFIER,
                JavaLexer.ASSIGN, JavaLexer.DECIMAL_LITERAL, JavaLexer.SEMI,
                JavaLexer.DOUBLE, JavaLexer.IDENTIFIER, JavaLexer.ASSIGN,
                JavaLexer.FLOAT_LITERAL, JavaLexer.MUL, JavaLexer.DECIMAL_LITERAL,
                JavaLexer.DIV, JavaLexer.IDENTIFIER, JavaLexer.SEMI, JavaLexer.EOF
        );
        assertTokenTypes("boolean flag = true; int x = 100; double y = 2.5 * 4 / x;", expectedTypes);
    }

    @Test
    void testCommentsAndWhitespace() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.INT, JavaLexer.IDENTIFIER,
                JavaLexer.ASSIGN, JavaLexer.DECIMAL_LITERAL, JavaLexer.SEMI, JavaLexer.EOF
        );
        assertTokenTypes("// This is a comment\nint x = 10; /* Another comment */", expectedTypes);
    }
}
