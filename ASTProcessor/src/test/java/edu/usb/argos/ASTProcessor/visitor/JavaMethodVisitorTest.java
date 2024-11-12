package edu.usb.argos.ASTProcessor.visitor;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.application.analyzers.code.CodeMetricsAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.application.analyzers.complexity.ComplexityMetricsAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.application.analyzers.depedency.DependencyAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.*;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JavaMethodVisitorTest {
    @Mock
    private CodeMetricsAnalyzer codeAnalyzer;

    @Mock
    private ComplexityMetricsAnalyzer complexityAnalyzer;

    @Mock
    private DependencyAnalyzer dependencyAnalyzer;

    private JavaMethodVisitor visitor;

    @BeforeEach
    void setUp() {
        visitor = new JavaMethodVisitor(codeAnalyzer, complexityAnalyzer, dependencyAnalyzer);
    }

    @Test
    void visitMethod_SimpleMethod_ReturnsCorrectMethodInfo() {
        String code = """
            public class TestClass {
                public int testMethod(String param1, int param2) {
                    return 0;
                }
            }
            """;
        JavaParser.MethodDeclarationContext methodContext = getMethodContext(code);

        when(codeAnalyzer.analyze(any())).thenReturn(new CodeMetrics(3, 1, 0, 0));
        when(complexityAnalyzer.analyze(any())).thenReturn(new ComplexityMetrics(1, 0, 1));
        when(dependencyAnalyzer.analyze(any())).thenReturn(new DependencyInfo(List.of(), List.of(), List.of()));

        MethodInfo result = visitor.visitMethod(methodContext);

        assertNotNull(result);
        assertEquals("testMethod", result.getName());
        assertEquals("int", result.getReturnType());
        assertEquals(List.of("public"), result.getModifiers());
        assertEquals(2, result.getParameters().size());
        verify(codeAnalyzer).analyze(methodContext);
        verify(complexityAnalyzer).analyze(methodContext);
        verify(dependencyAnalyzer).analyze(methodContext);
    }

    @Test
    void getParameters_MethodWithParameters_ReturnsCorrectParameters() {
        String code = """
            public class TestClass {
                private void testMethod(String name, int age) {}
            }
            """;
        JavaParser.MethodDeclarationContext methodContext = getMethodContext(code);

        List<ParameterInfo> parameters = visitor.getParameters(methodContext);

        assertEquals(2, parameters.size());
        assertEquals("name", parameters.get(0).getName());
        assertEquals("String", parameters.get(0).getType());
        assertEquals("age", parameters.get(1).getName());
        assertEquals("int", parameters.get(1).getType());
    }

    @Test
    void getMethodModifiers_MethodWithMultipleModifiers_ReturnsAllModifiers() {
        String code = """
            public class TestClass {
                public static final void testMethod() {}
            }
            """;
        JavaParser.MethodDeclarationContext methodContext = getMethodContext(code);

        List<String> modifiers = visitor.getMethodModifiers(methodContext);

        assertEquals(3, modifiers.size());
        assertTrue(modifiers.contains("public"));
        assertTrue(modifiers.contains("static"));
        assertTrue(modifiers.contains("final"));
    }

    @Test
    void getReturnType_VoidMethod_ReturnsVoid() {
        String code = """
            public class TestClass {
                void testMethod() {}
            }
            """;
        JavaParser.MethodDeclarationContext methodContext = getMethodContext(code);

        String returnType = visitor.getReturnType(methodContext);

        assertEquals("void", returnType);
    }

    @Test
    void visitMethod_InvalidContext_ReturnsNull() {
        MethodInfo result = visitor.visitMethod(mock(JavaParser.ClassDeclarationContext.class));

        assertNull(result);
    }

    private JavaParser.MethodDeclarationContext getMethodContext(String code) {
        JavaLexer lexer = new JavaLexer(CharStreams.fromString(code));
        JavaParser parser = new JavaParser(new CommonTokenStream(lexer));
        return parser.compilationUnit()
                .typeDeclaration(0)
                .classDeclaration()
                .classBody()
                .classBodyDeclaration(0)
                .memberDeclaration()
                .methodDeclaration();
    }

}
