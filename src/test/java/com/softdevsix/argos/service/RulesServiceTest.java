package com.softdevsix.argos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.softdevsix.argos.domain.*;
import com.softdevsix.argos.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class RulesServiceTests {

    @Mock
    private RulesRepoImpl rulesRepo;

    @InjectMocks
    private RulesService rulesService;

    @Mock
    private ProjectValidator projectValidator;

    @Mock
    private RulesValidator rulesValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleRules() {
        RulesRequestMap mockRequest = new RulesRequestMap();
        Integer projectId = 1;

        BestPracticesRules bestPractices = new BestPracticesRules();
        CodeComplexityRules codeComplexity = new CodeComplexityRules();
        CodeQualityRules codeQuality = new CodeQualityRules();
        CodeSmellsRules codeSmells = new CodeSmellsRules();
        CodingStandardsRules codingStandards = new CodingStandardsRules();
        CoverageRules coverage = new CoverageRules();

        Rules mockRules = Rules.builder()
                .bestPractices(bestPractices)
                .codeComplexity(codeComplexity)
                .codeQuality(codeQuality)
                .codeSmells(codeSmells)
                .codingStandards(codingStandards)
                .coverage(coverage)
                .build();

        Project mockProject = new Project();
        mockProject.setId(projectId);

        when(rulesValidator.validate(mockRequest)).thenReturn(mockRules);
        when(projectValidator.validate(projectId)).thenReturn(mockProject);

        when(rulesRepo.createRule(mockRules, mockProject)).thenReturn(projectId);
        when(rulesRepo.fetchRule(any(Integer.class))).thenReturn(Optional.of(mockRules));

        Rules result = rulesService.handleRules(mockRequest, projectId);

        assertNotNull(result, "The result should not be null.");
        assertEquals(mockRules, result, "The result should be the same Rules object.");

    }
}
