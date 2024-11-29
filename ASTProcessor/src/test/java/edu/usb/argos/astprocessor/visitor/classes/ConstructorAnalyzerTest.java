package edu.usb.argos.astprocessor.visitor.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
import static org.mockito.Mockito.*;

class ConstructorAnalyzerTest {

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
    void testIsConstructorDeclaration_NullMemberContext() {
        JavaConstructorVisitor visitorNullMember = new JavaConstructorVisitor();
        JavaParser.MemberDeclarationContext memberCtx = null;

        boolean result = invokeIsConstructorDeclaration(visitorNullMember, memberCtx);

        assertFalse(result);
    }

    @Test
    void testIsConstructorDeclaration_MemberContextWithoutConstructor() {
        JavaConstructorVisitor visitorMemberContext = new JavaConstructorVisitor();
        JavaParser.MemberDeclarationContext memberCtx = mock(JavaParser.MemberDeclarationContext.class);
        when(memberCtx.constructorDeclaration()).thenReturn(null);

        boolean result = invokeIsConstructorDeclaration(visitorMemberContext, memberCtx);

        assertFalse(result);
    }

    @Test
    void testIsConstructorDeclaration_MemberContextWithConstructor() {
        JavaConstructorVisitor visitorConstructor = new JavaConstructorVisitor();
        JavaParser.MemberDeclarationContext memberCtx = mock(JavaParser.MemberDeclarationContext.class);
        when(memberCtx.constructorDeclaration()).thenReturn(mock(JavaParser.ConstructorDeclarationContext.class));

        boolean result = invokeIsConstructorDeclaration(visitorConstructor, memberCtx);

        assertTrue(result);
    }

    private boolean invokeIsConstructorDeclaration(JavaConstructorVisitor visitor, JavaParser.MemberDeclarationContext memberCtx) {
        try {
            Method method = JavaConstructorVisitor.class.getDeclaredMethod("isConstructorDeclaration", JavaParser.MemberDeclarationContext.class);
            method.setAccessible(true);
            return (boolean) method.invoke(visitor, memberCtx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetModifiersWithoutModifiers() throws Exception {
        JavaParser.ClassBodyDeclarationContext mockBodyCtx = mock(JavaParser.ClassBodyDeclarationContext.class);
        when(mockBodyCtx.modifier()).thenReturn(List.of());
        Method method = JavaConstructorVisitor.class.getDeclaredMethod("getModifiers", JavaParser.ClassBodyDeclarationContext.class);
        method.setAccessible(true); 

        List<String> modifiers = (List<String>) method.invoke(visitor, mockBodyCtx);
        assertTrue(modifiers.isEmpty());
    }

    @Test
    void testGetModifiersNullModifiers() throws Exception {
        JavaParser.ClassBodyDeclarationContext mockBodyCtx = mock(JavaParser.ClassBodyDeclarationContext.class);
        when(mockBodyCtx.modifier()).thenReturn(null);
        Method method = JavaConstructorVisitor.class.getDeclaredMethod("getModifiers", JavaParser.ClassBodyDeclarationContext.class);
        method.setAccessible(true);

        List<String> modifiers = (List<String>) method.invoke(visitor, mockBodyCtx);
        assertTrue(modifiers.isEmpty());
    }

    @Test
    void testVisitConstructors_WithConstructors() {
        String classContent = """
                class TestClass {
                    TestClass() {}
                    TestClass(int value) {}
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        assertTrue(classBody.isPresent());

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor.visitConstructors(classBody.get());

        assertEquals(2, constructors.size()); 
        assertEquals("TestClass", constructors.get(0).getName());
        assertEquals("TestClass", constructors.get(1).getName());
    }

    @Test
    void testVisitConstructors_WithoutConstructors() {
        String classContent = """
                class TestClass {
                    void method() {}
                    int field;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        assertTrue(classBody.isPresent());

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor.visitConstructors(classBody.get());

        assertTrue(constructors.isEmpty()); 
    }

    @Test
    void testVisitConstructors_WithEmptyClass() {
        String classContent = """
                class TestClass {
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        assertTrue(classBody.isPresent());

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor.visitConstructors(classBody.get());

        assertTrue(constructors.isEmpty());
    }

    @Test
    void testGetModifiers_WithModifiers() {
        String classContent = """
                class TestClass {
                    public TestClass() {}
                    private TestClass(int value) {}
                    protected TestClass(String value) {}
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor.visitConstructors(classBody.get());
        List<String> modifiers1 = constructors.get(0).getModifiers();
        List<String> modifiers2 = constructors.get(1).getModifiers();
        List<String> modifiers3 = constructors.get(2).getModifiers();
        
        assertTrue(classBody.isPresent());
        assertEquals(3, constructors.size());
        assertEquals(List.of("public"), modifiers1);
        assertEquals(List.of("private"), modifiers2);
        assertEquals(List.of("protected"), modifiers3);
    }

    @Test
    void testGetModifiers_WithoutModifiers() {
        String classContent = """
                class TestClass {
                    TestClass() {}
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor.visitConstructors(classBody.get());
        List<String> modifiers = constructors.get(0).getModifiers();
        
        assertTrue(classBody.isPresent());
        assertEquals(1, constructors.size());
        assertTrue(modifiers.isEmpty());
    }

    @Test
    void testGetBodyStatements_WithStatements() {
        String classContent = """
                class TestClass {
                    public TestClass() {
                        System.out.println(y);
                        System.out.println(x);
                    }
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor.visitConstructors(classBody.get());
        List<JavaParser.StatementContext> bodyStatements = constructors.get(0).getBodyStatements();
        
        assertEquals(1, constructors.size());
        assertTrue(classBody.isPresent());
        assertEquals(2, bodyStatements.size());
        assertEquals("System.out.println(y);", bodyStatements.get(0).getText());
        assertEquals("System.out.println(x);", bodyStatements.get(1).getText());
    }

    @Test
    void testGetBodyStatements_WithoutStatements() {
        String classContent = """
                class TestClass {
                    public TestClass() {
                    }
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor.visitConstructors(classBody.get());
        List<JavaParser.StatementContext> bodyStatements = constructors.get(0).getBodyStatements();
        
        assertTrue(classBody.isPresent());
        assertEquals(1, constructors.size());
        assertTrue(bodyStatements.isEmpty());
    }

    @Test
    void testGetBodyStatements_NullBlock() {
        String classContent = """
                class TestClass {
                    public TestClass() {}
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = visitor.visitConstructors(classBody.get());
        List<JavaParser.StatementContext> bodyStatements = constructors.get(0).getBodyStatements();
        
        assertTrue(classBody.isPresent());
        assertEquals(1, constructors.size());
        assertTrue(bodyStatements.isEmpty());
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
