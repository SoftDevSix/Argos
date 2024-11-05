package edu.usb.argos.ASTProcessor.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaLexerTest {

    private List<Token> tokenize(String input) {
        JavaLexer lexer = new JavaLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        return tokens.getTokens();
    }

    @Test
    void testKeywordToken() {
        List<Token> tokens = tokenize("class if for while");
        assertEquals(JavaLexer.CLASS, tokens.get(0).getType());
        assertEquals(JavaLexer.WS, tokens.get(1).getType());
        assertEquals(JavaLexer.IF, tokens.get(2).getType());
        assertEquals(JavaLexer.WS, tokens.get(3).getType());
        assertEquals(JavaLexer.FOR, tokens.get(4).getType());
        assertEquals(JavaLexer.WS, tokens.get(5).getType());
        assertEquals(JavaLexer.WHILE, tokens.get(6).getType());
    }

    @Test
    void testOperatorsToken() {
        List<Token> tokens = tokenize("+-*/ = == != <= >= && || ++ -- /=");
        assertEquals(JavaLexer.ADD, tokens.get(0).getType());
        assertEquals(JavaLexer.SUB, tokens.get(1).getType());
        assertEquals(JavaLexer.MUL, tokens.get(2).getType());
        assertEquals(JavaLexer.DIV, tokens.get(3).getType());
        assertEquals(JavaLexer.WS, tokens.get(4).getType());
        assertEquals(JavaLexer.ASSIGN, tokens.get(5).getType());
        assertEquals(JavaLexer.WS, tokens.get(6).getType());
        assertEquals(JavaLexer.EQUAL, tokens.get(7).getType());
        assertEquals(JavaLexer.WS, tokens.get(8).getType());
        assertEquals(JavaLexer.NOTEQUAL, tokens.get(9).getType());
        assertEquals(JavaLexer.WS, tokens.get(10).getType());
        assertEquals(JavaLexer.LE, tokens.get(11).getType());
        assertEquals(JavaLexer.WS, tokens.get(12).getType());
        assertEquals(JavaLexer.GE, tokens.get(13).getType());
        assertEquals(JavaLexer.WS, tokens.get(14).getType());
        assertEquals(JavaLexer.AND, tokens.get(15).getType());
        assertEquals(JavaLexer.WS, tokens.get(16).getType());
        assertEquals(JavaLexer.OR, tokens.get(17).getType());
        assertEquals(JavaLexer.WS, tokens.get(18).getType());
        assertEquals(JavaLexer.INC, tokens.get(19).getType());
        assertEquals(JavaLexer.WS, tokens.get(20).getType());
        assertEquals(JavaLexer.DEC, tokens.get(21).getType());
    }

    @Test
    void testLiteralsToken() {
        List<Token> tokens = tokenize("123 3.14 true false 'c' \"string\"");
        assertEquals(JavaLexer.DECIMAL_LITERAL, tokens.get(0).getType());
        assertEquals(JavaLexer.FLOAT_LITERAL, tokens.get(2).getType());
        assertEquals(JavaLexer.BOOL_LITERAL, tokens.get(4).getType());
        assertEquals(JavaLexer.BOOL_LITERAL, tokens.get(6).getType());
        assertEquals(JavaLexer.CHAR_LITERAL, tokens.get(8).getType());
        assertEquals(JavaLexer.STRING_LITERAL, tokens.get(10).getType());
    }

    @Test
    void testSeparatorsToken() {
        List<Token> tokens = tokenize("( ) { } ; , .");
        assertEquals(JavaLexer.LPAREN, tokens.get(0).getType());
        assertEquals(JavaLexer.RPAREN, tokens.get(2).getType());
        assertEquals(JavaLexer.LBRACE, tokens.get(4).getType());
        assertEquals(JavaLexer.RBRACE, tokens.get(6).getType());
        assertEquals(JavaLexer.SEMI, tokens.get(8).getType());
        assertEquals(JavaLexer.COMMA, tokens.get(10).getType());
        assertEquals(JavaLexer.DOT, tokens.get(12).getType());
    }

    @Test
    void testIdentifiersToken() {
        List<Token> tokens = tokenize("myVariable _myVariable myVariable123");
        assertEquals(JavaLexer.IDENTIFIER, tokens.get(0).getType());
        assertEquals(JavaLexer.IDENTIFIER, tokens.get(2).getType());
        assertEquals(JavaLexer.IDENTIFIER, tokens.get(4).getType());
    }

    @Test
    void testInvalidIdentifiersToken() {
        List<Token> tokens = tokenize("123myVariable $InvalidId");
        assertEquals(JavaLexer.DECIMAL_LITERAL, tokens.get(0).getType());
        assertEquals(JavaLexer.IDENTIFIER, tokens.get(1).getType());
    }

    @Test
    void testWhitespaceAndComments() {
        List<Token> tokens = tokenize("int x = 42; // this is a comment\n/* multi-line\n comment */");
        assertEquals(JavaLexer.INT, tokens.get(0).getType());
        assertEquals(JavaLexer.IDENTIFIER, tokens.get(2).getType());
        assertEquals(JavaLexer.ASSIGN, tokens.get(4).getType());
        assertEquals(JavaLexer.DECIMAL_LITERAL, tokens.get(6).getType());
        assertEquals(JavaLexer.SEMI, tokens.get(7).getType());
        assertEquals(JavaLexer.LINE_COMMENT, tokens.get(9).getType());
        assertEquals(JavaLexer.WS, tokens.get(10).getType());
        assertEquals(JavaLexer.COMMENT, tokens.get(11).getType());

        assertEquals(13, tokens.size());
    }

    @Test
    void testMultipleTokensTogetherFilteringWS() {
        List<Token> tokens = tokenize("int main = 5; if (main == 5) { return true; }")
                .stream().filter(token -> token.getChannel() == Token.DEFAULT_CHANNEL)
                .toList();;
        assertEquals(JavaLexer.INT, tokens.get(0).getType());
        assertEquals(JavaLexer.IDENTIFIER, tokens.get(1).getType());
        assertEquals(JavaLexer.ASSIGN, tokens.get(2).getType());
        assertEquals(JavaLexer.DECIMAL_LITERAL, tokens.get(3).getType());
        assertEquals(JavaLexer.SEMI, tokens.get(4).getType());
        assertEquals(JavaLexer.IF, tokens.get(5).getType());
        assertEquals(JavaLexer.LPAREN, tokens.get(6).getType());
        assertEquals(JavaLexer.IDENTIFIER, tokens.get(7).getType());
        assertEquals(JavaLexer.EQUAL, tokens.get(8).getType());
        assertEquals(JavaLexer.DECIMAL_LITERAL, tokens.get(9).getType());
        assertEquals(JavaLexer.RPAREN, tokens.get(10).getType());
        assertEquals(JavaLexer.LBRACE, tokens.get(11).getType());
        assertEquals(JavaLexer.RETURN, tokens.get(12).getType());
        assertEquals(JavaLexer.BOOL_LITERAL, tokens.get(13).getType());
        assertEquals(JavaLexer.SEMI, tokens.get(14).getType());
        assertEquals(JavaLexer.RBRACE, tokens.get(15).getType());
    }
}
