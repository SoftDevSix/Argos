package edu.usb.argos.astprocessor.ConstructorAnalyzerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import org.antlr.v4.runtime.CharStream;
import org.junit.jupiter.api.BeforeAll;
import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;

public class ConstructorAnalyzerTest {

    private static JavaConstructorVisitor visitor;

    @BeforeAll
    public static void init() {
        visitor = new JavaConstructorVisitor();
    }

    private Optional<JavaParser.ClassBodyContext> getClassFromText(String classBody) {
        CharStream input = CharStreams.fromString(classBody);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);

        JavaParser.CompilationUnitContext compilationUnitContext = parser.compilationUnit();

        if (!compilationUnitContext.typeDeclaration().isEmpty()) {
            JavaParser.TypeDeclarationContext typeDecl = compilationUnitContext.typeDeclaration().get(0);
            if (typeDecl.classDeclaration() != null) {
                return Optional.ofNullable(typeDecl.classDeclaration().classBody());
            }
        }

        return Optional.empty();
    }

    @Test
    void testVisitConstructorsWithModifiers() {
        String classBody = """
                    public class MyClass {
                        public MyClass() {}
                        private MyClass(String name) {}
                        protected MyClass(int age) {}
                    }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor
                .visitConstructors(classFound.get());

        assertEquals(3, constructors.size());
        assertEquals("public", constructors.get(0).getModifiers().get(0));
        assertEquals("private", constructors.get(1).getModifiers().get(0));
        assertEquals("protected", constructors.get(2).getModifiers().get(0));
    }

    @Test
    void testVisitConstructorsWithoutModifiers() {
        String classBody = """
                    public class MyClass {
                        MyClass() {}
                        MyClass(String name) {}
                        MyClass(int age) {}
                    }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor
                .visitConstructors(classFound.get());

        assertEquals(3, constructors.size());
        for (ConstructorInformation<JavaParser.StatementContext> constructor : constructors) {
            assertTrue(constructor.getModifiers().isEmpty());
        }
    }

    @Test
    void testVisitConstructorsWithParameters() {
        String classBody = """
                    public class MyClass {
                        public MyClass(String name, int age) {}
                    }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor
                .visitConstructors(classFound.get());
        ConstructorInformation<JavaParser.StatementContext> constructor = constructors.get(0);

        assertEquals(1, constructors.size());
        assertEquals("MyClass", constructor.getName());
        assertEquals(2, constructor.getParameters().size());
        assertEquals("String name", constructor.getParameters().get(0));
        assertEquals("int age", constructor.getParameters().get(1));
    }

    @Test
    void testVisitConstructorsCalculatorClass() {
        String classBody = """
                    public class Calculator {
                       private int num1;
                       private int num2;
                       public Calculator(int num1, int num2) {
                           this.num1 = num1;
                           this.num2 = num2;
                       }
                    }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor
                .visitConstructors(classFound.get());
        ConstructorInformation<JavaParser.StatementContext> constructor = constructors.get(0);
        List<JavaParser.StatementContext> bodyStatements = constructor.getBodyStatements();

        assertEquals(1, constructors.size());
        assertEquals("Calculator", constructor.getName());
        assertEquals(2, constructor.getParameters().size());
        assertEquals("int num1", constructor.getParameters().get(0));
        assertEquals("int num2", constructor.getParameters().get(1));

        assertEquals(2, bodyStatements.size());
        assertEquals("this.num1=num1;", bodyStatements.get(0).getText());
        assertEquals("this.num2=num2;", bodyStatements.get(1).getText());

    }

    @Test
    void testVisitConstructorsCircleClassWithDefaultConstructor() {
        String classBody = """
                    public class Circle {
                       private int ratio;

                       public Circle() {}

                       public Circle(int ratio) {
                           this.ratio = ratio;
                       }
                    }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor
                .visitConstructors(classFound.get());
        ConstructorInformation<JavaParser.StatementContext> parameterizedConstructor = constructors.get(0);
        List<JavaParser.StatementContext> bodyStatements = parameterizedConstructor.getBodyStatements();
        ConstructorInformation<JavaParser.StatementContext> parameterizedSecondConstructor = constructors.get(1);
        List<JavaParser.StatementContext> secondBodyStatements = parameterizedSecondConstructor
                .getBodyStatements();

        assertEquals(2, constructors.size());
        assertEquals("Circle", constructors.get(0).getName());
        assertEquals(0, constructors.get(0).getParameters().size());
        assertEquals(0, constructors.get(0).getBodyStatements().size());

        assertEquals("Circle", constructors.get(1).getName());
        assertEquals(1, constructors.get(1).getParameters().size());
        assertEquals("int ratio", constructors.get(1).getParameters().get(0));
        assertEquals(1, constructors.get(1).getBodyStatements().size());
        assertEquals("this.ratio=ratio;", secondBodyStatements.get(0).getText());
    }

    @Test
    void testVisitConstructorsCircleClassWithMethodCallInConstructor() {
        String classBody = """
                    public class Circle {
                       private int ratio;

                       public Circle() {}

                       public Circle(int ratio) {
                           this.ratio = ratio;
                           someOperation();
                       }

                       public void someOperation() {}
                    }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor
                .visitConstructors(classFound.get());
        ConstructorInformation<JavaParser.StatementContext> parameterizedConstructor = constructors.get(1);
        List<JavaParser.StatementContext> bodyStatements = parameterizedConstructor.getBodyStatements();

        assertEquals(2, constructors.size());
        assertEquals("Circle", constructors.get(0).getName());
        assertEquals(0, constructors.get(0).getParameters().size());
        assertEquals(0, constructors.get(0).getBodyStatements().size());

        assertEquals("Circle", constructors.get(1).getName());
        assertEquals(1, constructors.get(1).getParameters().size());
        assertEquals("int ratio", constructors.get(1).getParameters().get(0));
        assertEquals(2, constructors.get(1).getBodyStatements().size());
        assertEquals("this.ratio=ratio;", bodyStatements.get(0).getText());
        assertEquals("someOperation();", bodyStatements.get(1).getText());
    }
}
