package edu.usb.argos.ASTProcessor.infrastructure.analyzers.directoryAnalyzers;

import edu.usb.argos.ASTProcessor.application.analyzers.IDirectoryAnalyzer;
import edu.usb.argos.ASTProcessor.application.analyzers.IFileAnalyzer;
import edu.usb.argos.ASTProcessor.application.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.infrastructure.utils.SourceTreeAnalyzer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class DirectoryAnalyzerByPath<TAst> implements IDirectoryAnalyzer<Path, TAst> {

    private final IFileAnalyzer<Path, TAst> fileAnalyzer;
    private final SourceTreeAnalyzer treeAnalyzer;

    @Override
    public List<TAst> analyzeDirectory(Path path) {
        List<Path> javaFilePaths = treeAnalyzer.getJavaFiles(path);
        TAst ast;
        List<TAst> astFiles = new ArrayList<>();

        for (Path javaFilePath : javaFilePaths) {
            try {
                ast = fileAnalyzer.readFile(javaFilePath);
                astFiles.add(ast);
            } catch (FileAnalyzerException exception) {
                log.error(exception.getMessage(), exception);
            }
        }

        return astFiles;
    }
}
