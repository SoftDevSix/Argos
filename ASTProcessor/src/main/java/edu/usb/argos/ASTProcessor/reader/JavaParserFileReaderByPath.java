package edu.usb.argos.ASTProcessor.reader;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import edu.usb.argos.ASTProcessor.reader.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.reader.interfaces.IFileAnalyzer;
import edu.usb.argos.ASTProcessor.reader.interfaces.IValidationStrategy;
import edu.usb.argos.ASTProcessor.reader.validations.PathValidationStrategy;

import java.io.IOException;
import java.nio.file.Path;

public class JavaParserFileReaderByPath implements IFileAnalyzer<Path, CompilationUnit> {
    private final IValidationStrategy<Path> validationStrategy;

    public JavaParserFileReaderByPath() {
        this.validationStrategy = new PathValidationStrategy();
    }

    public JavaParserFileReaderByPath(IValidationStrategy<Path> validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

    @Override
    public CompilationUnit readFile(Path codePath) throws FileAnalyzerException {
        try {
            validationStrategy.validate(codePath);
            return parseFile(codePath);
        } catch (IOException e) {
            throw new FileAnalyzerException("Error reading file: " + codePath, e);
        } catch (ParseProblemException e) {
            throw new FileAnalyzerException("Error parsing file: " + codePath, e);
        }
    }

    private CompilationUnit parseFile(Path codePath) throws IOException, ParseProblemException {
        SourceRoot sourceRoot = new SourceRoot(codePath.getParent());
        return sourceRoot.parse("", codePath.getFileName().toString());
    }
}
