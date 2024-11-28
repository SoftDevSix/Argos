package edu.usb.argos.astprocessor.reader;

import edu.usb.argos.astprocessor.reader.application.services.FileReaderByText;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AntlrFileReaderByTextTest {

    private FileReaderByText fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderByText();
    }

    @Test
    void testReadFileWithValidContent() {
        String content = "class Example {}";
        Optional<ParseTree> parseTree = fileReader.readFile(content);
        assertTrue(parseTree.isPresent());
    }

    @Test
    void testReadFileWithEmptyContent() {
        String content = "";
        Exception exception = assertThrows(Exception.class, () -> fileReader.readFile(content));
        assertEquals("error to parse empty input", exception.getMessage(),
                "Should throw parse content error for empty input");
    }

    @Test
    void testReadFileWithAConcreteClass() {
        String content = """
                public class Calculator {
                    public int add(int a, int b) {
                        return a + b;
                    }
                
                    public int subtract(int a, int b) {
                        return a - b;
                    }
                }""";
        Optional<ParseTree> parseContent = fileReader.readFile(content);
        assertTrue(parseContent.isPresent());
    }

    @Test
    void testReadFileWithNullContent() {
        Exception exception = assertThrows(Exception.class, () -> fileReader.readFile(null));
        assertEquals("error to parse null input", exception.getMessage(),
                "Should throw read file error for null input");
    }
}
