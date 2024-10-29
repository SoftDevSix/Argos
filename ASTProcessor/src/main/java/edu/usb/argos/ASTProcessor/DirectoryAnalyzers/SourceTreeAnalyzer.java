package edu.usb.argos.ASTProcessor.DirectoryAnalyzers;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceTreeAnalyzer {

    public List<Path> getJavaFiles(Path directoryPath) throws Exception {
        String javaExtension = ".java";
        return getFilesByExtension(directoryPath, javaExtension);
    }

    private List<Path> getFilesByExtension(Path directoryPath, String extension) throws Exception {
        if (!Files.exists(directoryPath)) {
            throw new NoSuchFileException("No found directory for: " + directoryPath);
        }

        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("The path provided is not a directory: " + directoryPath);
        }

        try (Stream<Path> stream = Files.walk(directoryPath)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.toString().endsWith(extension))
                    .collect(Collectors.toList());
        }
    }

}
