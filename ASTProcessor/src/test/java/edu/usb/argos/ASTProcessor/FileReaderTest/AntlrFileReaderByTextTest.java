<<<<<<< HEAD
package edu.usb.argos.ASTProcessor.FileReaderTest;

import edu.usb.argos.ASTProcessor.reader.application.services.FileReaderByText;
=======
package test.java.edu.usb.argos.ASTProcessor.FileReaderTest;

import main.java.edu.usb.argos.ASTProcessor.FileReaderByText.FileReaderByText;
>>>>>>> ae7a897 (feat: add file reader antlr by text)

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
<<<<<<< HEAD

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AntlrFileReaderByTextTest {
    private FileReaderByText fileReader;
=======
import static org.junit.jupiter.api.Assertions.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;

public class AntlrFileReaderByTextTest {
private FileReaderByText fileReader;
>>>>>>> ae7a897 (feat: add file reader antlr by text)

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderByText();
    }

    @Test
    void testReadFileWithValidContent() throws Exception {
<<<<<<< HEAD
        String content = "class Example {}";
        Optional<ParseTree> parseTree = fileReader.readFile(content);
        assertTrue(parseTree.isPresent());
        assertNotNull(parseTree.get(), "ParseTree should not be null for valid content");
=======
        String content = "class Example {}"; 
        ParseTree parseTree = fileReader.readFile(content);
        assertNotNull(parseTree, "ParseTree should not be null for valid content");
>>>>>>> ae7a897 (feat: add file reader antlr by text)
    }

    @Test
    void testReadFileWithEmptyContent() {
        String content = "";
        Exception exception = assertThrows(Exception.class, () -> fileReader.readFile(content));
<<<<<<< HEAD
        assertEquals("error to parse empty input", exception.getMessage(),
                "Should throw parse content error for empty input");
=======
        assertEquals("error to read file", exception.getMessage(), "Should throw parse content error for empty input");
>>>>>>> ae7a897 (feat: add file reader antlr by text)
    }

    @Test
    void testReadFileWithAConcreteClass() throws Exception {
        String content = "public class Calculator {\n" +
<<<<<<< HEAD
                "    public int add(int a, int b) {\n" +
                "        return a + b;\n" +
                "    }\n" +
                "\n" +
                "    public int subtract(int a, int b) {\n" +
                "        return a - b;\n" +
                "    }\n" +
                "}";
        Optional<ParseTree> parseContent = fileReader.readFile(content);
        assertTrue(parseContent.isPresent());
        assertNotNull(parseContent.get(), "ParseTree should not be null for valid content");
=======
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
>>>>>>> ae7a897 (feat: add file reader antlr by text)
    }

    @Test
    void testReadFileWithNullContent() {
        String content = null;
        Exception exception = assertThrows(Exception.class, () -> fileReader.readFile(content));
<<<<<<< HEAD
        assertEquals("error to parse null input", exception.getMessage(),
                "Should throw read file error for null input");
=======
        assertEquals("error to read file", exception.getMessage(), "Should throw read file error for null input");
>>>>>>> ae7a897 (feat: add file reader antlr by text)
    }
}
