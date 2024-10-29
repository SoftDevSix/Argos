package edu.usb.argos.ASTProcessor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;


public class JavaParserFileReaderByText implements IFileAnalyzer<CompilationUnit, String> {
    @Override
    public CompilationUnit read(String text) {
        JavaParser javaParser = new JavaParser(new ParserConfiguration());
        ParseResult<CompilationUnit> parseResult = javaParser.parse(text);
        if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
            CompilationUnit compilationUnit = parseResult.getResult().get();
            return compilationUnit;
        } else {
            throw new RuntimeException("Error to analyze de code");
        }
    }

    public void validateInput(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty.");
        }
    }

    public boolean isClassDeclaration(CompilationUnit compilationUnit) {
        for (TypeDeclaration<?> type : compilationUnit.getTypes()) {
            if (type instanceof ClassOrInterfaceDeclaration) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidJavaCodeByHeader(String text) {
        return text.contains("class") || text.contains("interface") || text.contains("enum");
    }
}
