package edu.usb.argos.ASTProcessor.reader.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.ASTAnalysisException;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.domain.interfaces.IDirectoryAnalyzer;
import edu.usb.argos.ASTProcessor.reader.domain.interfaces.IFileAnalyzer;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class DirectoryAnalyzerByText<TAst> implements IDirectoryAnalyzer<String[], TAst> {

    private final IFileAnalyzer<String, TAst> fileAnalyzer;

    @Override
    public List<TAst> analyzeDirectory(String[] sourceCode) {
        List<TAst> astFiles = new ArrayList<>();
        Optional<TAst> ast;

        for (String code : sourceCode) {
            try {
                ast = fileAnalyzer.readFile(code);
                ast.ifPresent(astFiles::add);
            } catch (FileAnalyzerException exception) {
                String errorMessage = "Error getting AST from the source";
                throw new ASTAnalysisException(errorMessage, exception);
            }
        }

        return astFiles;
    }

}
