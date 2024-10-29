package edu.usb.argos.ASTProcessor;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaParserExampleTest {

    @Test
    public void testGetMethodNames() throws Exception {

        String testFilePath = "src/test/resources/TestClass.java";
        String fileContent = """
            public class TestClass {
                public void exampleMethod() {}
            }
        """;
        Files.createDirectories(Paths.get("src/test/resources"));
        Files.writeString(Paths.get(testFilePath), fileContent);

        JavaParserExample parserExample = new JavaParserExample();
        List<String> methodNames = parserExample.getMethodNames(testFilePath);

        assertEquals(1, methodNames.size());
        assertEquals("exampleMethod", methodNames.get(0));

        Files.deleteIfExists(Paths.get(testFilePath));
    }
}