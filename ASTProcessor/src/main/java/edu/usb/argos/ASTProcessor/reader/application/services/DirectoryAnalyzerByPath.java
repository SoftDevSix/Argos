package edu.usb.argos.ASTProcessor.reader.application.services;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.ASTAnalysisException;
import edu.usb.argos.ASTProcessor.reader.domain.interfaces.IDirectoryAnalyzer;
import edu.usb.argos.ASTProcessor.reader.infraestructure.utils.SourceTreeAnalyzer;
import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.domain.interfaces.IFileAnalyzer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class DirectoryAnalyzerByPath<TAst> implements IDirectoryAnalyzer<Path, TAst> {

    private final IFileAnalyzer<Path, TAst> fileAnalyzer;
    private final SourceTreeAnalyzer treeAnalyzer;

    @Override
    public List<TAst> analyzeDirectory(Path path) {
        List<Path> javaFilePaths = treeAnalyzer.getJavaFiles(path);
        Optional<TAst> ast;
        List<TAst> astFiles = new ArrayList<>();

        for (Path javaFilePath : javaFilePaths) {
            try {
                ast = fileAnalyzer.readFile(javaFilePath);
                ast.ifPresent(astFiles::add);
            } catch (FileAnalyzerException exception) {
                String errorMessage = "Error getting AST from the source";
                throw new ASTAnalysisException(errorMessage, exception);
            }
        }

        return astFiles;
    }
}
