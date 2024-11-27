package edu.usb.argos.ASTProcessor.analyzer.codeSmells;

import edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces.INormalizer;
import edu.usb.argos.ASTProcessor.analyzer.infrastructure.normalizers.AntlrMethodNormalizer;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AntlrMethodNormalizerTest {

    private static INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer;

    @BeforeAll
    public static void setup() {
        methodNormalizer = new AntlrMethodNormalizer();
    }

    private List<JavaParser.MethodDeclarationContext> parseMethods(String code) {
        try {
            CharStream input = CharStreams.fromString(code);
            JavaLexer lexer = new JavaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            JavaParser.CompilationUnitContext tree = parser.compilationUnit();

            List<JavaParser.MethodDeclarationContext> methodDeclarations = new ArrayList<>();

            for (JavaParser.TypeDeclarationContext typeDecl : tree.typeDeclaration()) {
                if (typeDecl.classDeclaration() != null) {
                    JavaParser.ClassBodyContext classBody = typeDecl.classDeclaration().classBody();
                    for (JavaParser.ClassBodyDeclarationContext bodyDecl : classBody.classBodyDeclaration()) {
                        JavaParser.MemberDeclarationContext memberDecl = bodyDecl.memberDeclaration();
                        if (memberDecl != null) {
                            if (memberDecl.methodDeclaration() != null) {
                                methodDeclarations.add(memberDecl.methodDeclaration());
                            } else if (memberDecl.genericMethodDeclaration() != null) {
                                methodDeclarations.add(memberDecl.genericMethodDeclaration().methodDeclaration());
                            }
                        }
                    }
                }
            }

            return methodDeclarations;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Java code: " + e.getMessage(), e);
        }
    }

    @Test
    public void testNormalizeMethodAsTokens() {
        String code = """
                public class MyClass {
                    public void method5() {
                        for (int i = 0; i < 20; i++) { // 15
                            if (i > 10) {
                                System.out.println(i);
                                i += 20;
                                break;
                            }
                
                            method6();
                            System.out.println("Loop iteration: " + i);
                            System.out.println(method3("some name"));
                        }
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> methods = parseMethods(code);
        assertNotNull(methods, "Method context should not be null");

        Optional<JavaParser.MethodDeclarationContext> method = Optional.ofNullable(methods.get(0));
        assertTrue(method.isPresent());

        List<String> tokens = methodNormalizer.normalize(method.get());
        int expectedTokensSize = 66;

        assertEquals(expectedTokensSize, tokens.size());

//      FOR, LPAREN, INT, IDENTIFIER, ASSIGN, LITERAL, SEMI,
//      for    (      int    i           =       0        ;
//      IDENTIFIER, LT, LITERAL, SEMI, IDENTIFIER, INC, RPAREN, LBRACE
//      i           <     20      ;      i          ++    )       {

        List<String> expectedForTokens = List.of("FOR","LPAREN","INT","IDENTIFIER","ASSIGN","LITERAL","SEMI","IDENTIFIER","LT","LITERAL","SEMI","IDENTIFIER","INC","RPAREN","LBRACE");
        int firstRowForLimit = 15;
        assertIterableEquals(expectedForTokens, tokens.subList(0, firstRowForLimit));

    }
}
