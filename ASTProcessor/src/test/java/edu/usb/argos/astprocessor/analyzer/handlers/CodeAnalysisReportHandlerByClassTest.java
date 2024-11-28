package edu.usb.argos.astprocessor.analyzer.handlers;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeAnalysisReport;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeAnalysisReportType;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CodeAnalysisReportHandlerByClassTest {

    private CodeSmellAnalysisByClass mockCodeSmellAnalysisByClass;
    private CodeAnalysisReportHandlerByClass handler;

    @BeforeEach
    void setUp() {
        mockCodeSmellAnalysisByClass = Mockito.mock(CodeSmellAnalysisByClass.class);
        handler = new CodeAnalysisReportHandlerByClass(mockCodeSmellAnalysisByClass);
    }

    @Test
    void testAddMethodWithExcessiveParameters() {
        handler.addMethodWithExcessiveParameters(10, 20);

        ArgumentCaptor<CodeAnalysisReport> captor = ArgumentCaptor.forClass(CodeAnalysisReport.class);
        verify(mockCodeSmellAnalysisByClass).addCodeAnalysisReport(captor.capture());

        CodeAnalysisReport report = captor.getValue();
        assertEquals(10, report.getStartLine());
        assertEquals(20, report.getEndLine());
        assertEquals(CodeAnalysisReportType.EXCESSIVE_PARAMETERS, report.getType());
        assertEquals("The method has too many parameters, making it difficult to understand and use.", report.getMessage());
    }

    @Test
    void testAddMethodWithMagicNumbers() {
        handler.addMethodWithMagicNumbers(30, 40);

        ArgumentCaptor<CodeAnalysisReport> captor = ArgumentCaptor.forClass(CodeAnalysisReport.class);
        verify(mockCodeSmellAnalysisByClass).addCodeAnalysisReport(captor.capture());

        CodeAnalysisReport report = captor.getValue();
        assertEquals(30, report.getStartLine());
        assertEquals(40, report.getEndLine());
        assertEquals(CodeAnalysisReportType.MAGIC_NUMBER, report.getType());
        assertEquals("Magic numbers were detected in the method, consider using constants with descriptive names.", report.getMessage());
    }

    @Test
    void testAddMethodWithMethodTooLong() {
        handler.addMethodWithMethodTooLong(50, 60);

        ArgumentCaptor<CodeAnalysisReport> captor = ArgumentCaptor.forClass(CodeAnalysisReport.class);
        verify(mockCodeSmellAnalysisByClass).addCodeAnalysisReport(captor.capture());

        CodeAnalysisReport report = captor.getValue();
        assertEquals(50, report.getStartLine());
        assertEquals(60, report.getEndLine());
        assertEquals(CodeAnalysisReportType.METHOD_TOO_LONG, report.getType());
        assertEquals("The method is too long, consider breaking it down into smaller, reusable methods.", report.getMessage());
    }

    @Test
    void testAddMethodWithNoDuplicatedCode() {
        handler.addMethodWithNoDuplicatedCode(70, 80);

        ArgumentCaptor<CodeAnalysisReport> captor = ArgumentCaptor.forClass(CodeAnalysisReport.class);
        verify(mockCodeSmellAnalysisByClass).addCodeAnalysisReport(captor.capture());

        CodeAnalysisReport report = captor.getValue();
        assertEquals(70, report.getStartLine());
        assertEquals(80, report.getEndLine());
        assertEquals(CodeAnalysisReportType.DUPLICATED_CODE, report.getType());
        assertEquals("Duplicate code was detected in the method, consider abstracting it or reusing functions.", report.getMessage());
    }
}
