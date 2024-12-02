package edu.usb.argos.astprocessor.reader.application.services;

import edu.usb.argos.astprocessor.reader.domain.exceptions.ASTAnalysisException;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IDirectoryAnalyzer;
import edu.usb.argos.astprocessor.reader.infraestructure.utils.SourceTreeAnalyzer;
import edu.usb.argos.astprocessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileAnalyzer;
import lombok.AllArgsConstructor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class DirectoryAnalyzerByPath<A> implements IDirectoryAnalyzer<Path, A> {

    private final IFileAnalyzer<Path, A> fileAnalyzer;
    private final SourceTreeAnalyzer treeAnalyzer;

    @Override
    public List<A> analyzeDirectory(Path path) {
        List<Path> javaFilePaths = treeAnalyzer.getJavaFiles(path);
        Optional<A> ast;
        List<A> astFiles = new ArrayList<>();

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
