package main.java.edu.usb.argos.ASTProcessor.FileReader;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.CompilationUnit;

public class FileAnalyzerValidator {
    public void validateInput(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty.");
        }
    }

    public boolean validateClassDeclaration(CompilationUnit compilationUnit) throws Exception {
        try {
            for (TypeDeclaration<?> type : compilationUnit.getTypes()) {
                if (type instanceof ClassOrInterfaceDeclaration) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new FileAnalyzerException("An unexpected error occurred on class declaration");
        }
    }

    public boolean validateJavaCodeByHeader(String text) throws Exception{
        try {
            return text.contains("class") || text.contains("interface") || text.contains("enum");
        } catch (Exception e) {
            throw new FileAnalyzerException("An unexpected error occurred on type of file");
        }
    }
}
