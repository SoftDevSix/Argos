package edu.usb.argos.ASTProcessor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class JavaParserExample {
    public List<String> getMethodNames(String filePath) {
        List<String> methodNames = new ArrayList<>();
        try {
            File file = new File(filePath);
            JavaParser parser = new JavaParser();
            CompilationUnit compilationUnit = parser.parse(file).getResult()
                    .orElseThrow(() -> new RuntimeException("Failed to parse the file"));

            compilationUnit.findAll(MethodDeclaration.class).forEach(method -> {
                methodNames.add(method.getNameAsString());
            });
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (RuntimeException e) {
            System.err.println("Error parsing file: " + e.getMessage());
        }
        return methodNames;
    }
}