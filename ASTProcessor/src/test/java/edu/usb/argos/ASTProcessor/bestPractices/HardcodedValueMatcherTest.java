package edu.usb.argos.ASTProcessor.bestPractices;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HardcodedValueMatcherTest {

    private static HardcodedValueMatcher matcher;

    @BeforeAll
    public static void setUp() {
        matcher = HardcodedValueMatcher.getInstance();
    }

    @Test
    public void testStringLiteralIsHardcoded() {
        assertTrue(matcher.isHardcoded("\"Hello, World!\""));
    }

    @Test
    public void testIntegerLiteralIsHardcoded() {
        assertTrue(matcher.isHardcoded("42"));
    }

    @Test
    public void testFloatingPointLiteralIsHardcoded() {
        assertTrue(matcher.isHardcoded("3.14"));
    }

    @Test
    public void testScientificNotationLiteralIsHardcoded() {
        assertTrue(matcher.isHardcoded("1.23e10"));
    }

    @Test
    public void testBooleanLiteralIsHardcoded() {
        assertTrue(matcher.isHardcoded("true"));
        assertTrue(matcher.isHardcoded("false"));
    }

    @Test
    public void testNullLiteralIsHardcoded() {
        assertTrue(matcher.isHardcoded("null"));
    }

    @Test
    public void testCharacterLiteralIsHardcoded() {
        assertTrue(matcher.isHardcoded("'a'"));
        assertTrue(matcher.isHardcoded("\"a\""));
    }

    @Test
    public void testNonLiteralIsNotHardcoded() {
        assertFalse(matcher.isHardcoded("variableName"));
        assertFalse(matcher.isHardcoded("methodCall()"));
    }

    @Test
    public void testWhitespaceAroundLiteral() {
        assertTrue(matcher.isHardcoded("   \"Hello, World!\"   "));
    }

    @Test
    public void testEmptyStringIsNotHardcoded() {
        assertFalse(matcher.isHardcoded(""));
    }
}
