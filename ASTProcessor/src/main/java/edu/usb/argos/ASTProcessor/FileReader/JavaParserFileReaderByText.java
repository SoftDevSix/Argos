package edu.usb.argos.ASTProcessor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import main.java.edu.usb.argos.ASTProcessor.FileReader.FileAnalyzerException;

public class JavaParserFileReaderByText implements IFileAnalyzer<CompilationUnit, String> {
    @Override
    public CompilationUnit read(String text) throws FileAnalyzerException {
        JavaParser javaParser = new JavaParser(new ParserConfiguration());
        ParseResult<CompilationUnit> parseResult = javaParser.parse(text);
        CompilationUnit compilationUnit = null;
        if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
            return compilationUnit = parseResult.getResult().get();
        } else {
            throw new FileAnalyzerException("Error to analyze de code");
        }
    }
}
