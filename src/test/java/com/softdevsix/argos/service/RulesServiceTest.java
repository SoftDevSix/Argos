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

        BestPractices_rules bestPractices = new BestPractices_rules();
        CodeComplexity_rules codeComplexity = new CodeComplexity_rules();
        CodeQuality_rules codeQuality = new CodeQuality_rules();
        CodeSmells_rules codeSmells = new CodeSmells_rules();
        CodingStandards_rules codingStandards = new CodingStandards_rules();
        Coverage_rules coverage = new Coverage_rules();

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
