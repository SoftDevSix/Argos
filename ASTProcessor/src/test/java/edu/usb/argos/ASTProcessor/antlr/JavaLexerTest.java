package edu.usb.argos.ASTProcessor.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaLexerTest {

    private List<Token> tokenize(String input) {
        JavaLexer lexer = new JavaLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        return tokens.getTokens();
    }

    private void assertTokenTypes(String input, List<Integer> expectedTypes) {
        List<Token> tokens = tokenize(input);
        List<Integer> actualTypes = new ArrayList<>(tokens.stream().map(Token::getType).toList());
        assertEquals(expectedTypes, actualTypes);
    }

    @Test
    void testKeywordToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.CLASS, JavaLexer.IF,
                JavaLexer.FOR, JavaLexer.WHILE, JavaLexer.EOF
        );
        assertTokenTypes("class if for while", expectedTypes);
    }

    @Test
    void testOperatorsToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.ADD, JavaLexer.SUB, JavaLexer.MUL, JavaLexer.DIV,
                JavaLexer.ASSIGN, JavaLexer.EQUAL, JavaLexer.NOTEQUAL, JavaLexer.LE,
                JavaLexer.GE, JavaLexer.AND, JavaLexer.OR, JavaLexer.INC, JavaLexer.DEC,
                JavaLexer.DIV_ASSIGN, JavaLexer.EOF
        );
        assertTokenTypes("+ - * / = == != <= >= && || ++ -- /=", expectedTypes);
    }

    @Test
    void testLiteralsToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.DECIMAL_LITERAL, JavaLexer.FLOAT_LITERAL,
                JavaLexer.BOOL_LITERAL, JavaLexer.BOOL_LITERAL,
                JavaLexer.CHAR_LITERAL, JavaLexer.STRING_LITERAL, JavaLexer.EOF
        );
        assertTokenTypes("123 3.14 true false 'c' \"string\"", expectedTypes);
    }

    @Test
    void testSeparatorsToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.LPAREN, JavaLexer.RPAREN,
                JavaLexer.LBRACE, JavaLexer.RBRACE,
                JavaLexer.SEMI, JavaLexer.COMMA,
                JavaLexer.DOT, JavaLexer.EOF
        );
        assertTokenTypes("( ) { } ; , .", expectedTypes);
    }

    @Test
    void testIdentifiersToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.IDENTIFIER, JavaLexer.IDENTIFIER,
                JavaLexer.IDENTIFIER, JavaLexer.EOF
        );
        assertTokenTypes("myVariable _myVariable myVariable123", expectedTypes);
    }

    @Test
    void testInvalidIdentifiersToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.DECIMAL_LITERAL, JavaLexer.IDENTIFIER,
                JavaLexer.IDENTIFIER, JavaLexer.EOF
        );
        assertTokenTypes("123myVariable $InvalidId", expectedTypes);
    }

    @Test
    void testWhitespaceAndComments() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.INT, JavaLexer.IDENTIFIER, JavaLexer.ASSIGN,
                JavaLexer.DECIMAL_LITERAL, JavaLexer.SEMI, JavaLexer.EOF
        );
        assertTokenTypes(
                "int x = 42; // this is a comment\n/* multi-line\n comment */",
                expectedTypes
        );
    }

    @Test
    void testMultipleTokensTogetherFilteringWS() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.INT, JavaLexer.IDENTIFIER, JavaLexer.ASSIGN,
                JavaLexer.DECIMAL_LITERAL, JavaLexer.SEMI,
                JavaLexer.IF, JavaLexer.LPAREN, JavaLexer.IDENTIFIER,
                JavaLexer.EQUAL, JavaLexer.DECIMAL_LITERAL,
                JavaLexer.RPAREN, JavaLexer.LBRACE, JavaLexer.RETURN,
                JavaLexer.BOOL_LITERAL, JavaLexer.SEMI,
                JavaLexer.RBRACE, JavaLexer.EOF
        );
        assertTokenTypes(
                "int main = 5; if (main == 5) { return true; }",
                expectedTypes
        );
    }
}
