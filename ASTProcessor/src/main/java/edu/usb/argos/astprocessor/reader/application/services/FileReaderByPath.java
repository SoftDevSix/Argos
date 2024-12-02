package edu.usb.argos.astprocessor.reader.application.services;

import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.astprocessor.reader.domain.exceptions.ParserException;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileAnalyzer;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileValidationStrategy;
import edu.usb.argos.astprocessor.reader.infraestructure.validation.PathValidationStrategy;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Generated
@Slf4j
public class FileReaderByPath implements IFileAnalyzer<Path, ParserRuleContext> {
    private final IFileValidationStrategy<Path> validationStrategy;

    public FileReaderByPath() {
        this.validationStrategy = new PathValidationStrategy();
    }

    public FileReaderByPath(IFileValidationStrategy<Path> validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

    @Override
    public Optional<ParserRuleContext> readFile(Path codePath) throws FileAnalyzerException {
        try {
            validationStrategy.validate(codePath);
            String content = Files.readString(codePath);

            return Optional.ofNullable(parseContent(content));
        } catch (IOException e) {
            log.error("Error reading file: {}", codePath, e);
            throw new FileAnalyzerException("Error reading file: " + codePath, e);
        } catch (ParserException e) {
            log.error("Error parsing file: {}", codePath, e);
            throw new FileAnalyzerException("Error parsing file: " + codePath, e);
        } catch (Exception e) {
            log.error("Error analyzing file: {}", codePath, e);
            throw new FileAnalyzerException("Error analyzing file: " + codePath, e);
        }
    }

    private ParserRuleContext parseContent(String content) throws ParserException {
        try {
            CharStream input = CharStreams.fromString(content);
            JavaLexer lexer = new JavaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            JavaParser.CompilationUnitContext context = parser.compilationUnit();

            return context.typeDeclaration(0).classDeclaration();
        } catch (Exception e) {
            log.error("Error parsing content", e);
            throw new ParserException("Error to parse tree", e);
        }
    }
}
