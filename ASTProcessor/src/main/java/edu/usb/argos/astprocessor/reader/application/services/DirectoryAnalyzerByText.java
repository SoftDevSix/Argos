package edu.usb.argos.astprocessor.reader.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.usb.argos.astprocessor.reader.domain.exceptions.ASTAnalysisException;

import edu.usb.argos.astprocessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IDirectoryAnalyzer;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileAnalyzer;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class DirectoryAnalyzerByText<A> implements IDirectoryAnalyzer<String[], A> {

    private final IFileAnalyzer<String, A> fileAnalyzer;

    @Override
    public List<A> analyzeDirectory(String[] sourceCode) {
        List<A> astFiles = new ArrayList<>();
        Optional<A> ast;

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
