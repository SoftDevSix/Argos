package edu.usb.argos.astprocessor.reader.application.services;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;

import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileAnalyzer;
import edu.usb.argos.astprocessor.reader.domain.exceptions.FileReaderException;
import edu.usb.argos.astprocessor.reader.infraestructure.validation.FileReaderValidation;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FileReaderByText implements IFileAnalyzer<String, ParserRuleContext> {
    public Optional<ParserRuleContext> readFile(String content) throws FileReaderException {
        return Optional.ofNullable(parseContent(content));
    }

    private ParserRuleContext parseContent(String content) throws FileReaderException {
        FileReaderValidation fileReaderValidation = new FileReaderValidation();
        fileReaderValidation.validateFileReaderByText(content);
        CharStream input = CharStreams.fromString(content);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        JavaParser.CompilationUnitContext context = parser.compilationUnit();

        return context.typeDeclaration(0).classDeclaration();
    }
}
