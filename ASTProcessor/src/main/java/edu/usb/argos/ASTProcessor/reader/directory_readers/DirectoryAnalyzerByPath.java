package edu.usb.argos.ASTProcessor.reader.directory_readers;

import edu.usb.argos.ASTProcessor.reader.interfaces.IDirectoryAnalyzer;
import edu.usb.argos.ASTProcessor.reader.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.interfaces.IFileAnalyzer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DirectoryAnalyzerByPath<TAst> implements IDirectoryAnalyzer<Path, TAst> {

    private static final Logger LOGGER = Logger.getLogger(DirectoryAnalyzerByPath.class.getName() );
    private final IFileAnalyzer<Path, TAst> fileAnalyzer;

    public DirectoryAnalyzerByPath(IFileAnalyzer<Path, TAst> fileAnalyzer) {
        this.fileAnalyzer = fileAnalyzer;
    }

    @Override
    public List<TAst> analyzeDirectory(Path path) {
        // TODO: use Source Tree analyzer
        Path[] javaFilePaths = new Path[0];
        TAst ast;
        List<TAst> astFiles = new ArrayList<>();

        for (Path javaFilePath : javaFilePaths) {
            try {
                ast = fileAnalyzer.readFile(javaFilePath);
                astFiles.add(ast);
            } catch (FileAnalyzerException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
            }
        }

        return astFiles;
    }
}
