package com.softdevsix.argos;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.softdevsix.argos.codeSmells.detectors.UnusedVariablesDetector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static final String PATH = "/home/fundacion/projects/Argos/src/main/resources/Hello.java";

    public static void main(String[] args) throws IOException {

        CompilationUnit cu = StaticJavaParser.parse(Files.newInputStream(Paths.get(PATH)));

        UnusedVariablesDetector unusedVariablesDetector = new UnusedVariablesDetector();


        List<String> unusedVariables = unusedVariablesDetector.detect(cu);
        System.out.println("Variables:");
        if (unusedVariables.isEmpty()) {
            System.out.println("NADA.");
        } else {
            unusedVariables.forEach(var -> System.out.println("- " + var));
        }
    }
}
