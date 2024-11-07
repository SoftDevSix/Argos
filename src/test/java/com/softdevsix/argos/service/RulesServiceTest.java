package com.softdevsix.argos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.softdevsix.argos.domain.*;
import com.softdevsix.argos.repository.RulesRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import java.util.Optional;


@SpringBootTest
class RulesServiceTests {

    @Mock
    private RulesRepoImpl rulesRepo;

    @Mock
    private ProjectValidator projectValidator;

    @Mock
    private RulesValidator rulesValidator;

    @InjectMocks
    private RulesService rulesService;

    private RulesRequestMap rulesRequestMap;
    private Project mockProject;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rulesRequestMap = new RulesRequestMap();
        mockProject = new Project();
        mockProject.setId(1);

        Rules mockRules = new Rules();

        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLength(true);
        codeQuality.setMaxLineLengthLimit(120);
        mockRules.setCodeQuality(codeQuality);

        BestPractices bestPractices = new BestPractices();
        bestPractices.setNoHardcodedValues(false);
        mockRules.setBestPractices(bestPractices);

        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setCyclomaticComplexityLimit(true);
        codeComplexity.setMaxCyclomaticComplexity(15);
        codeComplexity.setNestingDepthLimit(true);
        codeComplexity.setMaxNestingDepth(5);
        mockRules.setCodeComplexity(codeComplexity);

        when(projectValidator.validate(mockProject.getId())).thenReturn(mockProject);
        when(rulesRepo.fetchRule(mockProject.getId())).thenReturn(Optional.of(mockRules));
        when(rulesValidator.validate(any(RulesRequestMap.class))).thenReturn(mockRules);

    }

    @Test
    void testHandleRules_withValidCodeQuality() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLength(true);
        codeQuality.setMaxLineLengthLimit(120);
        rulesRequestMap.setCodeQuality(codeQuality);

        rulesService.handleRules(rulesRequestMap,mockProject.getId());
        Rules validatedRules = rulesService.getRules(mockProject.getId());

        assertNotNull(validatedRules.getCodeQuality(), "CodeQuality should not be null");
        assertEquals(120, validatedRules.getCodeQuality().getMaxLineLengthLimit(), "Max line length should be 120");
    }


    @Test
    void testHandleRules_withValidCodeComplexity() {

        rulesService.handleRules(rulesRequestMap,mockProject.getId());
        Rules validatedRules = rulesService.getRules(mockProject.getId());

        assertNotNull(validatedRules.getCodeComplexity(), "CodeComplexity should not be null");
        assertEquals(15, validatedRules.getCodeComplexity().getMaxCyclomaticComplexity(), "Max cyclomatic complexity should be 15");
        assertEquals(5, validatedRules.getCodeComplexity().getMaxNestingDepth(), "Max nesting depth should be 5");
    }


    @Test
    void testGetRules_afterHandlingRules() {
        
        rulesService.handleRules(rulesRequestMap,mockProject.getId());
        Rules validatedRules = rulesService.getRules(mockProject.getId());

        assertNotNull(validatedRules, "Rules should not be null after handling");
        assertEquals(120, validatedRules.getCodeQuality().getMaxLineLengthLimit(), "Max line length limit should be set to 100");
    }

    @Test
    void testHandleRules_withValidBestPractices() {
        
        rulesService.handleRules(rulesRequestMap,mockProject.getId());
        Rules validatedRules = rulesService.getRules(mockProject.getId());

        assertNotNull(validatedRules.getBestPractices(), "BestPractices should not be null");
        assertEquals(false, validatedRules.getBestPractices().isNoHardcodedValuesEnabled(), "NoHardcodedValues should be false");
    }
}