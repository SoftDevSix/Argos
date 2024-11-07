package com.softdevsix.argos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    //@Mock
    //private RulesValidator rulesValidator;

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
    }

    @Test
    void testHandleRules_withInvalidMaxLineLengthLimit() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLength(true);
        codeQuality.setMaxLineLengthLimit(-1);

        rulesRequestMap.setCodeQuality(codeQuality);

        assertThrows(IllegalArgumentException.class, () -> {
            rulesService.handleRules(rulesRequestMap,mockProject.getId());
        }, "Should throw exception for negative max line length limit");
    }

    @Test
    void testHandleRules_withInvalidCyclomaticComplexityLimit() {
        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setCyclomaticComplexityLimit(true);
        codeComplexity.setMaxCyclomaticComplexity(-5);

        rulesRequestMap.setCodeComplexity(codeComplexity);

        assertThrows(IllegalArgumentException.class, () -> {
            rulesService.handleRules(rulesRequestMap,mockProject.getId());
        }, "Should throw exception for negative cyclomatic complexity limit");
    }
}
