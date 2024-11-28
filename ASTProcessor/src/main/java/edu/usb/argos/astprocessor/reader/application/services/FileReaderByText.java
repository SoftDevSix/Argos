package edu.usb.argos.astprocessor.reader.application.services;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;

import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileAnalyzer;
import edu.usb.argos.astprocessor.reader.domain.exceptions.FileReaderException;
import edu.usb.argos.astprocessor.reader.infraestructure.validation.FileReaderValidation;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FileReaderByText implements IFileAnalyzer<String, ParseTree> {
    public Optional<ParseTree> readFile(String content) throws FileReaderException {
        return Optional.of(parseContent(content));
    }

    private ParseTree parseContent(String content) throws FileReaderException {
        FileReaderValidation fileReaderValidation = new FileReaderValidation();
        fileReaderValidation.validateFileReaderByText(content);
        try {
            CharStream input = CharStreams.fromString(content);
            JavaLexer lexer = new JavaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);

            return parser.compilationUnit();
        } catch (Exception e) {
            throw new FileReaderException("Error to parse content");
        }
    }
}
