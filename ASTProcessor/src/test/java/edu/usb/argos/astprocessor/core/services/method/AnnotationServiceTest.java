package edu.usb.argos.astprocessor.core.services.method;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.services.method.AnnotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnnotationServiceTest {
    private AnnotationService annotationService;

    @Mock
    private JavaParser.ClassBodyDeclarationContext bodyCtx;

    @Mock
    private JavaParser.ModifierContext modifier1;

    @Mock
    private JavaParser.ModifierContext modifier2;

    @Mock
    private JavaParser.ClassOrInterfaceModifierContext classModifier1;

    @Mock
    private JavaParser.ClassOrInterfaceModifierContext classModifier2;

    @Mock
    private JavaParser.AnnotationContext annotation1;

    @Mock
    private JavaParser.AnnotationContext annotation2;

    @BeforeEach
    void setUp() {
        annotationService = new AnnotationService();
    }

    @Test
    void extractAnnotationNullContextReturnsEmptyList() {
        Optional<List<String>> result = annotationService.extractAnnotation(null);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    void extractAnnotationNoModifiersReturnsEmptyList() {
        when(bodyCtx.modifier()).thenReturn(null);

        Optional<List<String>> result = annotationService.extractAnnotation(bodyCtx);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    void extractAnnotationEmptyModifiersListReturnsEmptyList() {
        when(bodyCtx.modifier()).thenReturn(Collections.emptyList());

        Optional<List<String>> result = annotationService.extractAnnotation(bodyCtx);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    void extractAnnotationModifiersWithNoAnnotationsReturnsEmptyList() {
        List<JavaParser.ModifierContext> modifiers = Arrays.asList(modifier1, modifier2);
        when(bodyCtx.modifier()).thenReturn(modifiers);
        when(modifier1.classOrInterfaceModifier()).thenReturn(null);
        when(modifier2.classOrInterfaceModifier()).thenReturn(null);

        Optional<List<String>> result = annotationService.extractAnnotation(bodyCtx);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    void extractAnnotationModifiersWithSomeNullClassModifiersReturnsPartialList() {
        List<JavaParser.ModifierContext> modifiers = Arrays.asList(modifier1, modifier2);
        when(bodyCtx.modifier()).thenReturn(modifiers);

        when(modifier1.classOrInterfaceModifier()).thenReturn(classModifier1);
        when(modifier2.classOrInterfaceModifier()).thenReturn(null);

        when(classModifier1.annotation()).thenReturn(annotation1);
        when(annotation1.getText()).thenReturn("@Override");

        Optional<List<String>> result = annotationService.extractAnnotation(bodyCtx);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("@Override", result.get().get(0));
    }

    @Test
    void extractAnnotationMultipleAnnotationsReturnsAllAnnotations() {
        List<JavaParser.ModifierContext> modifiers = Arrays.asList(modifier1, modifier2);
        when(bodyCtx.modifier()).thenReturn(modifiers);

        when(modifier1.classOrInterfaceModifier()).thenReturn(classModifier1);
        when(modifier2.classOrInterfaceModifier()).thenReturn(classModifier2);

        when(classModifier1.annotation()).thenReturn(annotation1);
        when(classModifier2.annotation()).thenReturn(annotation2);

        when(annotation1.getText()).thenReturn("@Override");
        when(annotation2.getText()).thenReturn("@Deprecated");

        Optional<List<String>> result = annotationService.extractAnnotation(bodyCtx);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertTrue(result.get().contains("@Override"));
        assertTrue(result.get().contains("@Deprecated"));
    }

    @Test
    void extractAnnotationNullAnnotationInClassModifierSkipsNullAnnotation() {
        List<JavaParser.ModifierContext> modifiers = Arrays.asList(modifier1);
        when(bodyCtx.modifier()).thenReturn(modifiers);
        when(modifier1.classOrInterfaceModifier()).thenReturn(classModifier1);
        when(classModifier1.annotation()).thenReturn(null);

        Optional<List<String>> result = annotationService.extractAnnotation(bodyCtx);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    void extractAnnotationMixedModifiersAndAnnotationsReturnsOnlyAnnotations() {
        List<JavaParser.ModifierContext> modifiers = Arrays.asList(modifier1, modifier2);
        when(bodyCtx.modifier()).thenReturn(modifiers);

        when(modifier1.classOrInterfaceModifier()).thenReturn(classModifier1);
        when(classModifier1.annotation()).thenReturn(annotation1);
        when(annotation1.getText()).thenReturn("@Test");

        when(modifier2.classOrInterfaceModifier()).thenReturn(classModifier2);
        when(classModifier2.annotation()).thenReturn(null);

        Optional<List<String>> result = annotationService.extractAnnotation(bodyCtx);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("@Test", result.get().get(0));
    }
}
