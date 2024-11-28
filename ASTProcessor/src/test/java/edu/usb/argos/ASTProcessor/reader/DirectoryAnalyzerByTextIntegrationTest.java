package edu.usb.argos.astprocessor.reader;

import edu.usb.argos.astprocessor.reader.application.services.DirectoryAnalyzerByText;
import edu.usb.argos.astprocessor.reader.application.services.FileReaderByText;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DirectoryAnalyzerByTextIntegrationTest {

    private DirectoryAnalyzerByText<ParseTree> directoryAnalyzer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        FileReaderByText fileReaderByText = new FileReaderByText();
        directoryAnalyzer = new DirectoryAnalyzerByText<>(fileReaderByText);
    }

    @Test
    void testAnalyzeDirectory_WithNestedSubdirectories() throws Exception {
        Path dir1 = tempDir.resolve("dir1");
        Path dir2 = dir1.resolve("dir2");
        Files.createDirectories(dir2);

        Path javaFile1 = createJavaFile(dir1.resolve("File1.java"), "public class File1 {}");
        Path javaFile2 = createJavaFile(dir2.resolve("File2.java"), "public class File2 { void method() {} }");

        String[] sourceCode = {
                Files.readString(javaFile1),
                Files.readString(javaFile2)
        };

        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(1));
    }

    @Test
    void testAnalyzeDirectory_WithMultipleSubdirectories() throws Exception {
        Path subDirA = tempDir.resolve("subDirA");
        Path subDirB = subDirA.resolve("subDirB");
        Path subDirC = subDirB.resolve("subDirC");
        Files.createDirectories(subDirC);

        Path javaFile1 = createJavaFile(subDirA.resolve("FileA.java"), "public class FileA {}");
        Path javaFile2 = createJavaFile(subDirB.resolve("FileB.java"), "public class FileB { int value = 0; }");
        Path javaFile3 = createJavaFile(subDirC.resolve("FileC.java"), """
                public class FileC {
                    public void complexMethod() {
                        System.out.println("Complex structure!");
                    }
                }
                """);

        String[] sourceCode = {
                Files.readString(javaFile1),
                Files.readString(javaFile2),
                Files.readString(javaFile3)
        };

        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));
    }

    @Test
    void testAnalyzeDirectory_WithFlatStructure() throws Exception {
        Path javaFile1 = createJavaFile(tempDir.resolve("File1.java"), "public class File1 { void method() {} }");
        Path javaFile2 = createJavaFile(tempDir.resolve("File2.java"), "public class File2 { int value = 42; }");

        String[] sourceCode = {
                Files.readString(javaFile1),
                Files.readString(javaFile2)
        };

        List<ParseTree> result = directoryAnalyzer.analyzeDirectory(sourceCode);

        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(1));
    }

    private Path createJavaFile(Path filePath, String content) throws Exception {
        Files.writeString(filePath, content);
        return filePath;
    }
}
