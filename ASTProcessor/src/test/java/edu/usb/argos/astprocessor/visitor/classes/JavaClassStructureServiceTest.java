package edu.usb.argos.astprocessor.visitor.classes;

import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassStructureService;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.usb.argos.astprocessor.antlr.JavaParser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaClassStructureServiceTest {
    private JavaClassStructureService structureService;
    private JavaParser.CompilationUnitContext compilationUnit;

    @BeforeEach
    void setUp() {
        String testClass =
                """
                        public class TestClass extends BaseClass implements Interface1, Interface2 {
                            private String field;
                            public void method() {}
                        }""";

        parseClass(testClass);

        structureService = new JavaClassStructureService();
    }

    private JavaParser.CompilationUnitContext parseClass(String classSource) {
        CharStream input = CharStreams.fromString(classSource);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        compilationUnit = parser.compilationUnit();
        return compilationUnit;
    }

    private JavaParser.ClassDeclarationContext parseClassDeclarationContext(String classSource) {
        return parseClass(classSource).typeDeclaration(0).classDeclaration();
    }

    @Test
    void getSuperClassShouldReturnCorrectSuperClass() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        Optional<String> superClass = structureService.getSuperClass(ctx);

        assertTrue(superClass.isPresent());
        assertEquals("BaseClass", superClass.get());
    }

    @Test
    void getImplementedInterfacesShouldReturnCorrectInterfaces() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<String> interfaces = structureService.getImplementedInterfaces(ctx);

        assertEquals(2, interfaces.size());
        assertTrue(interfaces.contains("Interface1"));
        assertTrue(interfaces.contains("Interface2"));
    }

    @Test
    void getSuperClassShouldReturnEmptyWhenNoSuperClass() {
        String classSource = """
                public class TestClass {
                    private int field;
                    public void method() {}
                }
                """;

        JavaParser.ClassDeclarationContext ctx = parseClassDeclarationContext(classSource);
        Optional<String> superClass = structureService.getSuperClass(ctx);

        assertTrue(superClass.isEmpty());
    }

    @Test
    void getImplementedInterfacesShouldReturnEmptyWhenNoInterfaces() {
        String classSource = """
                public class TestClass extends BaseClass {
                    private int field;
                    public void method() {}
                }
                """;

        JavaParser.ClassDeclarationContext ctx = parseClassDeclarationContext(classSource);
        List<String> interfaces = structureService.getImplementedInterfaces(ctx);

        assertTrue(interfaces.isEmpty());
    }

    @Test
    void getImplementedInterfacesShouldReturnAllInterfaces() {
        String classSource = """
                public class TestClass implements Interface1, Interface2, Interface3 {
                    private int field;
                    public void method() {}
                }
                """;

        JavaParser.ClassDeclarationContext ctx = parseClassDeclarationContext(classSource);
        List<String> interfaces = structureService.getImplementedInterfaces(ctx);

        assertEquals(3, interfaces.size());
        assertTrue(interfaces.contains("Interface1"));
        assertTrue(interfaces.contains("Interface2"));
        assertTrue(interfaces.contains("Interface3"));
    }

    @Test
    void getSuperClassShouldWorkWithGenericSuperClass() {
        String classSource = """
                public class TestClass extends BaseClass<String> {
                    private int field;
                    public void method() {}
                }
                """;

        JavaParser.ClassDeclarationContext ctx = parseClassDeclarationContext(classSource);
        Optional<String> superClass = structureService.getSuperClass(ctx);

        assertTrue(superClass.isPresent());
        assertEquals("BaseClass<String>", superClass.get());
    }

    @Test
    void getImplementedInterfacesShouldWorkWithGenericInterfaces() {
        String classSource = """
                public class TestClass implements Interface1<Integer>, Interface2<String> {
                    private int field;
                    public void method() {}
                }
                """;

        JavaParser.ClassDeclarationContext ctx = parseClassDeclarationContext(classSource);
        List<String> interfaces = structureService.getImplementedInterfaces(ctx);

        assertEquals(2, interfaces.size());
        assertTrue(interfaces.contains("Interface1<Integer>"));
        assertTrue(interfaces.contains("Interface2<String>"));
    }
}
