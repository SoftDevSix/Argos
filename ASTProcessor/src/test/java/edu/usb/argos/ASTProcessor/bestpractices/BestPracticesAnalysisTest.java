package edu.usb.argos.ASTProcessor.bestpractices;

import edu.usb.argos.ASTProcessor.staticanalysis.AnalysisType;
import edu.usb.argos.ASTProcessor.staticanalysis.analysisresult.AnalysisResult;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.BestPracticesAnalyzer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BestPracticesAnalysisTest {
    @TempDir
    private Path tempDir;

    private Path createJavaFile() throws IOException {
        Path javaFile = tempDir.resolve("TestClass.java");
        String content = """
                package com.example;
                
                public class TestClass {
                    private int attribute;
                    private String text;
                    private String hardcoded = "Text";
                
                    public TestClass() {
                        attribute = 40;
                        text = "Hardcoded String Constructor";
                    }
                }
                """;
        Files.writeString(javaFile, content);
        return javaFile;
    }

    @Test
    public void fileAnalysis() throws IOException {
        Path file = createJavaFile();
        BestPracticesAnalyzer analyzer = new BestPracticesAnalyzer(file.toString());
        AnalysisResult result = analyzer.analyze();

        var expected = 3;
        assertEquals(expected, result.getReports().size());
        assertEquals(AnalysisType.BEST_PRACTICES, result.getAnalysisType());
        assertEquals(file.toString(), result.getFilePath());
    }


}
