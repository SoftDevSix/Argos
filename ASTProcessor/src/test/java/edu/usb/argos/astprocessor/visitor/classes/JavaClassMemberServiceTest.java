package edu.usb.argos.astprocessor.visitor.classes;

import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassMemberService;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JavaClassMemberServiceTest {
    @Mock
    private JavaMethodVisitor methodVisitor;
    @Mock
    private JavaAttributeVisitor attributeVisitor;
    @Mock
    private JavaConstructorVisitor constructorVisitor;

    private JavaClassMemberService memberService;
    private JavaParser.CompilationUnitContext compilationUnit;

    @BeforeEach
    void setUp() {
        String testClass =
                """
                        public class TestClass {
                            private String field1;
                            public Integer field2;
                            public TestClass() {}
                            public TestClass(String field1) { this.field1 = field1; }
                            public void method1() {}
                            private String method2() { return ""; }
                        }""";

        setUpTestClass(testClass);

        memberService = new JavaClassMemberService(methodVisitor, attributeVisitor, constructorVisitor);
    }

    private void setUpTestClass(String classCode) {
        CharStream input = CharStreams.fromString(classCode);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        compilationUnit = parser.compilationUnit();
    }

    @Test
     void getClassMethodsShouldReturnCorrectMethods() {
         JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

         MethodInformation<JavaParser.StatementContext>
                 method1 = MethodInformation.<
                         JavaParser.StatementContext>
                         builder()
                 .name("method1")
                 .returnType("void")
                 .modifiers(new ArrayList<>())
                 .parameters(new ArrayList<>())
                 .statements(new ArrayList<>())
                 .throwsExceptions(new ArrayList<>())
                 .annotations(new ArrayList<>())
                 .isVarArgs(false)
                 .build();

         MethodInformation<
                 JavaParser.StatementContext>
                 method2 = MethodInformation.<
                         JavaParser.StatementContext>
                         builder()
                 .name("method2")
                 .returnType("String")
                 .modifiers(new ArrayList<>())
                 .parameters(new ArrayList<>())
                 .statements(new ArrayList<>())
                 .throwsExceptions(new ArrayList<>())
                 .annotations(new ArrayList<>())
                 .isVarArgs(false)
                 .build();

         when(methodVisitor.visitMethodDeclaration(any())).thenReturn(method1).thenReturn(method2);

         List<MethodInformation<JavaParser.StatementContext>> methods = memberService.getClassMethods(ctx);

         assertEquals(2, methods.size());
         assertEquals("method1", methods.get(0).getName());
         assertEquals("method2", methods.get(1).getName());
     }

    @Test
    void getClassAttributesShouldReturnCorrectAttributes() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        JavaParser.ClassBodyContext bodyCtx = ctx.classBody();

        AttributeInformation attributeOne = AttributeInformation.builder()
                .name("field1")
                .type("String")
                .modifiers(List.of("private"))
                .build();

        AttributeInformation attributeTwo = AttributeInformation.builder()
                .name("field2")
                .type("String")
                .modifiers(List.of("private"))
                .build();

        List<AttributeInformation> expectedAttributes = Arrays.asList(
                attributeOne,
                attributeTwo
        );

        when(attributeVisitor.visitClassBody(bodyCtx)).thenReturn(expectedAttributes);

        List<AttributeInformation> attributes = memberService.getClassAttributes(ctx);

        assertEquals(2, attributes.size());
        assertEquals("field1", attributes.get(0).getName());
        assertEquals("field2", attributes.get(1).getName());
    }

    @Test
    void getClassConstructorsShouldReturnCorrectConstructors() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

        ConstructorInformation<JavaParser.StatementContext> constructorOne =
                ConstructorInformation.<JavaParser.StatementContext>builder()
                .name("TestClass")
                .modifiers(List.of("public"))
                .parameters(new ArrayList<>())
                .build();

        ConstructorInformation<JavaParser.StatementContext> constructorTwo =
                ConstructorInformation.<JavaParser.StatementContext>builder()
                .name("TestClass")
                .modifiers(List.of("public"))
                .parameters(List.of("String"))
                .build();

        when(constructorVisitor.visitConstructors(any())).thenReturn(Arrays.asList(constructorOne, constructorTwo));

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = memberService.getClassConstructors(ctx);

        assertEquals(2, constructors.size());

        assertEquals("TestClass", constructors.get(0).getName());
        assertEquals(List.of("public"), constructors.get(0).getModifiers());
        assertTrue(constructors.get(0).getParameters().isEmpty());

        assertEquals("TestClass", constructors.get(1).getName());
        assertEquals(List.of("public"), constructors.get(1).getModifiers());
        assertEquals(List.of("String"), constructors.get(1).getParameters());
    }

    @Test
    void getClassMethodsShouldReturnEmptyListWhenNoMethodsExist() {
        String testClass = """
            public class EmptyMethodsClass {
                private String field;
            }
            """;
        setUpTestClass(testClass);

        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<MethodInformation<JavaParser.StatementContext>> methods = memberService.getClassMethods(ctx);

        assertTrue(methods.isEmpty());
    }

    @Test
    void getClassAttributesShouldReturnEmptyListWhenNoAttributesExist() {
        String testClass = """
            public class EmptyAttributesClass {
                public void method() {}
            }
            """;
        setUpTestClass(testClass);

        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<AttributeInformation> attributes = memberService.getClassAttributes(ctx);

        assertTrue(attributes.isEmpty());
    }

    @Test
    void getClassConstructorsShouldReturnEmptyListWhenNoConstructorsExist() {
        String testClass = """
            public class NoConstructorClass {
                private String field;
                public void method() {}
            }
            """;
        setUpTestClass(testClass);

        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = memberService.getClassConstructors(ctx);

        assertTrue(constructors.isEmpty());
    }

    @Test
    void getClassMethodsShouldHandleInvalidContextGracefully() {
        String testInvalid = """
            interface InvalidContext {}
            """;
        setUpTestClass(testInvalid);

        JavaParser.InterfaceDeclarationContext ctx = compilationUnit.typeDeclaration(0).interfaceDeclaration();
        List<MethodInformation<JavaParser.StatementContext>> methods = memberService.getClassMethods(ctx);

        assertTrue(methods.isEmpty());
    }
}
