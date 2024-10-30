package com.softdevsix.argos.codeSmells.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import java.util.List;

public interface ICodeSmellDetector {
    List<String> detect(CompilationUnit cu);
}
