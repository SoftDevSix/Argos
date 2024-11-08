package edu.usb.argos.ASTProcessor.reader.directory_readers;

import edu.usb.argos.ASTProcessor.reader.interfaces.IPathValidator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceTreeAnalyzer {

    private IPathValidator pathValidator;

    public SourceTreeAnalyzer(IPathValidator pathValidator) {
        this.pathValidator = pathValidator;
    }

    public List<Path> getJavaFiles(Path directoryPath) throws Exception {
        String javaExtension = ".java";
        return getFilesByExtension(directoryPath, javaExtension);
    }

    private List<Path> getFilesByExtension(Path directoryPath, String extension) throws Exception {
        pathValidator.validatePath(directoryPath);

        try (Stream<Path> stream = Files.walk(directoryPath)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.toString().endsWith(extension))
                    .collect(Collectors.toList());
        }
    }

}
