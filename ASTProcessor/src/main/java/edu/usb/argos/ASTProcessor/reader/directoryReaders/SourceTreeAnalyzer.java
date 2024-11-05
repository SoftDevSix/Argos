package edu.usb.argos.ASTProcessor.reader.directoryReaders;

import edu.usb.argos.ASTProcessor.reader.interfaces.IPathValidator;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SourceTreeAnalyzer {

    private final IPathValidator pathValidator;
    private final String JAVA_EXTENSION = ".java";

    public SourceTreeAnalyzer(IPathValidator pathValidator) {
        this.pathValidator = pathValidator;
    }

    public List<Path> getJavaFiles(Path directoryPath) throws Exception {
        return getFilesByExtension(directoryPath, JAVA_EXTENSION);
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
