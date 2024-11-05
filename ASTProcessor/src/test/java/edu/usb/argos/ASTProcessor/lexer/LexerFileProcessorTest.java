package edu.usb.argos.ASTProcessor.lexer;

import org.junit.jupiter.api.Test;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerFileProcessorTest {
    private List<Token> getTokensFromFile(String input) throws IOException {
        LexerFileProcessor processor = new LexerFileProcessor();

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
                JavaLexer.IDENTIFIER, JavaLexer.WS, JavaLexer.DECIMAL_LITERAL, JavaLexer.WS,
                JavaLexer.ADD, JavaLexer.WS, JavaLexer.SUB, JavaLexer.WS, JavaLexer.MUL,
                JavaLexer.WS, JavaLexer.DIV, Token.EOF
        );
        assertTokenTypes("abc 123 + - * /", expectedTypes);
    }

    @Test
    void testGetTokensFromFile_WithConditionalAndOperators() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.IF, JavaLexer.WS, JavaLexer.LPAREN, JavaLexer.IDENTIFIER,
                JavaLexer.WS, JavaLexer.LT, JavaLexer.WS, JavaLexer.IDENTIFIER,
                JavaLexer.RPAREN, JavaLexer.WS, JavaLexer.LBRACE, JavaLexer.WS, JavaLexer.IDENTIFIER,
                JavaLexer.WS, JavaLexer.ADD_ASSIGN, JavaLexer.WS, JavaLexer.DECIMAL_LITERAL,
                JavaLexer.SEMI, JavaLexer.WS, JavaLexer.RBRACE, JavaLexer.EOF
        );
        assertTokenTypes("if (a < b) { a += 2; }", expectedTypes);
    }

    @Test
    void testGetTokensFromFile_WithClassAndMethodStructure() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.PUBLIC, JavaLexer.WS, JavaLexer.CLASS, JavaLexer.WS, JavaLexer.IDENTIFIER,
                JavaLexer.WS, JavaLexer.LBRACE, JavaLexer.WS, JavaLexer.VOID, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.LPAREN, JavaLexer.INT, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.RPAREN, JavaLexer.WS, JavaLexer.LBRACE, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.INC, JavaLexer.SEMI, JavaLexer.WS,
                JavaLexer.RBRACE, JavaLexer.WS, JavaLexer.RBRACE, JavaLexer.EOF
        );
        assertTokenTypes("public class MyClass { void doSomething(int a) { a++; } }", expectedTypes);
    }

    @Test
    void testGetTokensFromFile_WithLiteralsAndOperators() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.BOOLEAN, JavaLexer.WS, JavaLexer.IDENTIFIER, JavaLexer.WS,
                JavaLexer.ASSIGN, JavaLexer.WS, JavaLexer.BOOL_LITERAL, JavaLexer.SEMI, JavaLexer.WS,
                JavaLexer.INT, JavaLexer.WS, JavaLexer.IDENTIFIER, JavaLexer.WS,
                JavaLexer.ASSIGN, JavaLexer.WS, JavaLexer.DECIMAL_LITERAL, JavaLexer.SEMI,
                JavaLexer.WS, JavaLexer.DOUBLE, JavaLexer.WS, JavaLexer.IDENTIFIER, JavaLexer.WS,
                JavaLexer.ASSIGN, JavaLexer.WS, JavaLexer.FLOAT_LITERAL, JavaLexer.WS, JavaLexer.MUL,
                JavaLexer.WS, JavaLexer.DECIMAL_LITERAL, JavaLexer.WS, JavaLexer.DIV, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.SEMI, JavaLexer.EOF
        );
        assertTokenTypes("boolean flag = true; int x = 100; double y = 2.5 * 4 / x;", expectedTypes);
    }

    @Test
    void testCommentsAndWhitespace() throws IOException {
        List<Integer> expectedTypes = List.of(
                JavaLexer.LINE_COMMENT, JavaLexer.WS, JavaLexer.INT, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.WS, JavaLexer.ASSIGN,
                JavaLexer.WS, JavaLexer.DECIMAL_LITERAL,
                JavaLexer.SEMI, JavaLexer.WS, JavaLexer.COMMENT, JavaLexer.EOF
        );
        assertTokenTypes("// This is a comment\nint x = 10; /* Another comment */", expectedTypes);
    }

}
