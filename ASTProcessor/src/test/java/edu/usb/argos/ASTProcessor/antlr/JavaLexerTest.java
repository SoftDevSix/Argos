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
                JavaLexer.CLASS, JavaLexer.WS, JavaLexer.IF, JavaLexer.WS,
                JavaLexer.FOR, JavaLexer.WS, JavaLexer.WHILE, JavaLexer.EOF
        );
        assertTokenTypes("class if for while", expectedTypes);
    }

    @Test
    void testOperatorsToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.ADD, JavaLexer.WS, JavaLexer.SUB, JavaLexer.WS,
                JavaLexer.MUL, JavaLexer.WS, JavaLexer.DIV, JavaLexer.WS,
                JavaLexer.ASSIGN, JavaLexer.WS, JavaLexer.EQUAL, JavaLexer.WS,
                JavaLexer.NOTEQUAL, JavaLexer.WS, JavaLexer.LE, JavaLexer.WS,
                JavaLexer.GE, JavaLexer.WS, JavaLexer.AND, JavaLexer.WS, JavaLexer.OR,
                JavaLexer.WS, JavaLexer.INC, JavaLexer.WS, JavaLexer.DEC, JavaLexer.WS,
                JavaLexer.DIV_ASSIGN, JavaLexer.EOF
        );
        assertTokenTypes("+ - * / = == != <= >= && || ++ -- /=", expectedTypes);
    }

    @Test
    void testLiteralsToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.DECIMAL_LITERAL, JavaLexer.WS, JavaLexer.FLOAT_LITERAL, JavaLexer.WS,
                JavaLexer.BOOL_LITERAL, JavaLexer.WS, JavaLexer.BOOL_LITERAL, JavaLexer.WS,
                JavaLexer.CHAR_LITERAL, JavaLexer.WS, JavaLexer.STRING_LITERAL, JavaLexer.EOF
        );
        assertTokenTypes("123 3.14 true false 'c' \"string\"", expectedTypes);
    }

    @Test
    void testSeparatorsToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.LPAREN, JavaLexer.WS, JavaLexer.RPAREN, JavaLexer.WS,
                JavaLexer.LBRACE, JavaLexer.WS, JavaLexer.RBRACE, JavaLexer.WS,
                JavaLexer.SEMI, JavaLexer.WS, JavaLexer.COMMA, JavaLexer.WS,
                JavaLexer.DOT, JavaLexer.EOF
        );
        assertTokenTypes("( ) { } ; , .", expectedTypes);
    }

    @Test
    void testIdentifiersToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.IDENTIFIER, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.EOF
        );
        assertTokenTypes("myVariable _myVariable myVariable123", expectedTypes);
    }

    @Test
    void testInvalidIdentifiersToken() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.DECIMAL_LITERAL,
                JavaLexer.IDENTIFIER, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.EOF
        );
        assertTokenTypes("123myVariable $InvalidId", expectedTypes);
    }

    @Test
    void testWhitespaceAndComments() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.INT, JavaLexer.WS,
                JavaLexer.IDENTIFIER, JavaLexer.WS,
                JavaLexer.ASSIGN, JavaLexer.WS,
                JavaLexer.DECIMAL_LITERAL, JavaLexer.SEMI, JavaLexer.WS,
                JavaLexer.LINE_COMMENT, JavaLexer.WS,
                JavaLexer.COMMENT, JavaLexer.EOF
        );
        assertTokenTypes(
                "int x = 42; // this is a comment\n/* multi-line\n comment */",
                expectedTypes
        );
    }

    @Test
    void testMultipleTokensTogetherFilteringWS() {
        List<Integer> expectedTypes = List.of(
                JavaLexer.INT, JavaLexer.WS, JavaLexer.IDENTIFIER, JavaLexer.WS, JavaLexer.ASSIGN,
                JavaLexer.WS, JavaLexer.DECIMAL_LITERAL, JavaLexer.SEMI, JavaLexer.WS,
                JavaLexer.IF, JavaLexer.WS, JavaLexer.LPAREN, JavaLexer.IDENTIFIER,
                JavaLexer.WS, JavaLexer.EQUAL, JavaLexer.WS, JavaLexer.DECIMAL_LITERAL,
                JavaLexer.RPAREN, JavaLexer.WS, JavaLexer.LBRACE, JavaLexer.WS, JavaLexer.RETURN,
                JavaLexer.WS, JavaLexer.BOOL_LITERAL, JavaLexer.SEMI, JavaLexer.WS,
                JavaLexer.RBRACE, JavaLexer.EOF
        );
        assertTokenTypes(
                "int main = 5; if (main == 5) { return true; }",
                expectedTypes
        );
    }
}
