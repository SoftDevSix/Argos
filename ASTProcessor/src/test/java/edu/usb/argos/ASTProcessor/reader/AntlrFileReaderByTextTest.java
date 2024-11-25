package edu.usb.argos.ASTProcessor.reader;

import edu.usb.argos.ASTProcessor.reader.application.services.FileReaderByText;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;

public class AntlrFileReaderByTextTest {
    private FileReaderByText fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderByText();
    }

    @Test
    void testReadFileWithValidContent() throws Exception {
        String content = "class Example {}";
        ParseTree parseTree = fileReader.readFile(content);
        assertNotNull(parseTree, "ParseTree should not be null for valid content");
    }

    @Test
    void testReadFileWithEmptyContent() {
        String content = "";
        Exception exception = assertThrows(Exception.class, () -> fileReader.readFile(content));
        assertEquals("error to parse empty input", exception.getMessage(),
                "Should throw parse content error for empty input");
    }

    @Test
    void testReadFileWithAConcreteClass() throws Exception {
        String content = "public class Calculator {\n" +
                "    public int add(int a, int b) {\n" +
                "        return a + b;\n" +
                "    }\n" +
                "\n" +
                "    public int subtract(int a, int b) {\n" +
                "        return a - b;\n" +
                "    }\n" +
                "}";
        ParseTree parseContent = fileReader.readFile(content);
        assertNotNull(parseContent, "ParseTree should not be null for valid content");
    }

    @Test
    void testReadFileWithNullContent() {
        String content = null;
        Exception exception = assertThrows(Exception.class, () -> fileReader.readFile(content));
        assertEquals("error to parse null input", exception.getMessage(),
                "Should throw read file error for null input");
    }
}
