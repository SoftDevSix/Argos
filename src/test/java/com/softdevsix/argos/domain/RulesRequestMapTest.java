package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RulesRequestMapTest {
    @Test
    void testSetAndGetCodeQuality() {
        RulesRequestMap rulesRequestMap = new RulesRequestMap();
        CodeQuality codeQuality = new CodeQuality();

        rulesRequestMap.setCodeQuality(codeQuality);
        assertEquals(codeQuality, rulesRequestMap.getCodeQuality());
    }

    @Test
    void testSetAndGetBestPractices() {
        RulesRequestMap rulesRequestMap = new RulesRequestMap();
        BestPractices bestPractices = new BestPractices();

        rulesRequestMap.setBestPractices(bestPractices);
        assertEquals(bestPractices, rulesRequestMap.getBestPractices());
    }

    @Test
    void testSetAndGetCodeSmells() {
        RulesRequestMap rulesRequestMap = new RulesRequestMap();
        CodeSmells codeSmells = new CodeSmells();

        rulesRequestMap.setCodeSmells(codeSmells);
        assertEquals(codeSmells, rulesRequestMap.getCodeSmells());
    }

    @Test
    void testSetAndGetCodeComplexity() {
        RulesRequestMap rulesRequestMap = new RulesRequestMap();
        CodeComplexity codeComplexity = new CodeComplexity();

        rulesRequestMap.setCodeComplexity(codeComplexity);
        assertEquals(codeComplexity, rulesRequestMap.getCodeComplexity());
    }

    @Test
    void testSetAndGetCodingStandards() {
        RulesRequestMap rulesRequestMap = new RulesRequestMap();
        CodingStandards codingStandards = new CodingStandards();

        rulesRequestMap.setCodingStandards(codingStandards);
        assertEquals(codingStandards, rulesRequestMap.getCodingStandards());
    }

    @Test
    void testSetAndGetCoverage() {
        RulesRequestMap rulesRequestMap = new RulesRequestMap();
        Coverage coverage = new Coverage();

        rulesRequestMap.setCoverage(coverage);
        assertEquals(coverage, rulesRequestMap.getCoverage());
    }
}
