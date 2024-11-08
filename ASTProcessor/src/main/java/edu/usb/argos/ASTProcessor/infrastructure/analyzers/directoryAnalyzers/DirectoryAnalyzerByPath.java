package edu.usb.argos.ASTProcessor.infrastructure.analyzers.directoryAnalyzers;

import edu.usb.argos.ASTProcessor.application.analyzers.IDirectoryAnalyzer;
import edu.usb.argos.ASTProcessor.application.analyzers.IFileAnalyzer;
import edu.usb.argos.ASTProcessor.application.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.application.logging.IAppLogger;
import edu.usb.argos.ASTProcessor.infrastructure.utils.SourceTreeAnalyzer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DirectoryAnalyzerByPath<TAst> implements IDirectoryAnalyzer<Path, TAst> {

    private final IFileAnalyzer<Path, TAst> fileAnalyzer;
    private final SourceTreeAnalyzer treeAnalyzer;
    private final IAppLogger logger;

    public DirectoryAnalyzerByPath(IFileAnalyzer<Path, TAst> fileAnalyzer, SourceTreeAnalyzer treeAnalyzer,
            IAppLogger logger) {
        this.fileAnalyzer = fileAnalyzer;
        this.treeAnalyzer = treeAnalyzer;
        this.logger = logger;
    }

    @Override
    public List<TAst> analyzeDirectory(Path path) {
        List<Path> javaFilePaths = treeAnalyzer.getJavaFiles(path);
        TAst ast;
        List<TAst> astFiles = new ArrayList<>();

        for (Path javaFilePath : javaFilePaths) {
            try {
                ast = fileAnalyzer.readFile(javaFilePath);
                astFiles.add(ast);
            } catch (FileAnalyzerException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return astFiles;
    }
}
