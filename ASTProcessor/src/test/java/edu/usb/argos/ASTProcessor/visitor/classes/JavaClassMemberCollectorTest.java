package edu.usb.argos.ASTProcessor.visitor.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassMemberCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JavaClassMemberCollectorTest {
    @Mock
    private JavaMethodVisitor methodVisitor;
    @Mock
    private JavaAttributeVisitor attributeVisitor;
    @Mock
    private JavaConstructorVisitor constructorVisitor;

    private JavaClassMemberCollector memberCollector;
    private JavaParser.CompilationUnitContext compilationUnit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        String testClass =
                "public class TestClass {\n" +
                        "    private String field1;\n" +
                        "    public Integer field2;\n" +
                        "    public TestClass() {}\n" +
                        "    public TestClass(String field1) { this.field1 = field1; }\n" +
                        "    public void method1() {}\n" +
                        "    private String method2() { return \"\"; }\n" +
                        "}";

        CharStream input = CharStreams.fromString(testClass);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        compilationUnit = parser.compilationUnit();

        memberCollector = new JavaClassMemberCollector(methodVisitor, attributeVisitor, constructorVisitor);
    }

    @Test
    void getClassMethodsShouldReturnCorrectMethods() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

        MethodInformation<
                JavaParser.StatementContext,
                JavaParser.ExpressionContext,
                CommonTokenStream>
                method1 = MethodInformation.<
                        JavaParser.StatementContext,
                        JavaParser.ExpressionContext,
                        CommonTokenStream>
                        builder()
                .name("method1")
                .returnType("void")
                .modifiers(new ArrayList<>())
                .parameters(new ArrayList<>())
                .statements(new ArrayList<>())
                .expressions(new ArrayList<>())
                .tokens(null)
                .build();

        MethodInformation<
                JavaParser.StatementContext,
                JavaParser.ExpressionContext,
                CommonTokenStream>
                method2 = MethodInformation.<
                        JavaParser.StatementContext,
                        JavaParser.ExpressionContext,
                        CommonTokenStream>
                        builder()
                .name("method2")
                .returnType("String")
                .modifiers(new ArrayList<>())
                .parameters(new ArrayList<>())
                .statements(new ArrayList<>())
                .expressions(new ArrayList<>())
                .tokens(null)
                .build();

        when(methodVisitor.visitMethod(any())).thenReturn(method1).thenReturn(method2);

        List<MethodInformation<Void, Void, Void>> methods = memberCollector.getClassMethods(ctx);

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
                .modifiers(Arrays.asList("private"))
                .build();

        AttributeInformation attributeTwo = AttributeInformation.builder()
                .name("field2")
                .type("String")
                .modifiers(Arrays.asList("private"))
                .build();

        List<AttributeInformation> expectedAttributes = Arrays.asList(
                attributeOne,
                attributeTwo
        );

        when(attributeVisitor.visitClassBody(bodyCtx)).thenReturn(expectedAttributes);

        List<AttributeInformation> attributes = memberCollector.getClassAttributes(ctx);

        assertEquals(2, attributes.size());
        assertEquals("field1", attributes.get(0).getName());
        assertEquals("field2", attributes.get(1).getName());
    }

    @Test
    void getClassConstructorsShouldReturnCorrectConstructors() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

        ConstructorInformation constructorOne = ConstructorInformation.builder()
                .name("TestClass")
                .modifiers(Arrays.asList("public"))
                .parameters(new ArrayList<>())
                .build();

        ConstructorInformation constructorTwo = ConstructorInformation.builder()
                .name("TestClass")
                .modifiers(Arrays.asList("public"))
                .parameters(Arrays.asList("String"))
                .build();

        when(constructorVisitor.visitConstructors(any())).thenReturn(Arrays.asList(constructorOne, constructorTwo));

        List<ConstructorInformation> constructors = memberCollector.getClassConstructors(ctx);

        assertEquals(2, constructors.size());

        assertEquals("TestClass", constructors.get(0).getName());
        assertEquals(Arrays.asList("public"), constructors.get(0).getModifiers());
        assertTrue(constructors.get(0).getParameters().isEmpty());

        assertEquals("TestClass", constructors.get(1).getName());
        assertEquals(Arrays.asList("public"), constructors.get(1).getModifiers());
        assertEquals(Arrays.asList("String"), constructors.get(1).getParameters());
    }
}
