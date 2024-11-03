package com.softdevsix.argos.service;

import com.softdevsix.argos.domain.*;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.domain.RulesRequestMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public class RulesService {

    private Rules validatedRules;

    public RulesService() {
        this.validatedRules = new Rules();
    }

    public void handleRules(RulesRequestMap rulesRequestMap) {
       
        if (rulesRequestMap.getCodeQuality() != null) {
            CodeQuality codeQuality = rulesRequestMap.getCodeQuality();
            
            if (codeQuality.isMaxLineLengthEnabled() && codeQuality.getMaxLineLengthLimit() <= 0) {
                throw new IllegalArgumentException("El límite de longitud de línea debe ser positivo");
            }
            validatedRules.setCodeQuality(codeQuality);
        }

       
        if (rulesRequestMap.getBestPractices() != null) {
            BestPractices bestPractices = rulesRequestMap.getBestPractices();
            validatedRules.setBestPractices(bestPractices);
        }

        
        if (rulesRequestMap.getCodeSmells() != null) {
            CodeSmells codeSmells = rulesRequestMap.getCodeSmells();
            if (codeSmells.isMethodTooLongEnabled() && codeSmells.getMaxMethodLength() <= 0) {
                throw new IllegalArgumentException("El límite de longitud de método debe ser positivo");
            }
            validatedRules.setCodeSmells(codeSmells);
        }

        
        if (rulesRequestMap.getCodeComplexity() != null) {
            CodeComplexity codeComplexity = rulesRequestMap.getCodeComplexity();
            if (codeComplexity.isCyclomaticComplexityLimitEnabled() && codeComplexity.getMaxCyclomaticComplexity() <= 0) {
                throw new IllegalArgumentException("El límite de complejidad ciclomática debe ser positivo");
            }
            if (codeComplexity.isNestingDepthLimitEnabled() && codeComplexity.getMaxNestingDepth() <= 0) {
                throw new IllegalArgumentException("El límite de profundidad de anidación debe ser positivo");
            }
            validatedRules.setCodeComplexity(codeComplexity);
        }

       
        if (rulesRequestMap.getCodingStandards() != null) {
            CodingStandards codingStandards = rulesRequestMap.getCodingStandards();
            validatedRules.setCodingStandards(codingStandards);
        }

        
        if (rulesRequestMap.getCoverage() != null) {
            Coverage coverage = rulesRequestMap.getCoverage();
            if (coverage.isMinCoveragePercentageEnabled() && coverage.getCoverageThreshold() < 0) {
                throw new IllegalArgumentException("El umbral de cobertura debe ser no negativo");
            }
            validatedRules.setCoverage(coverage);
        }
    }

    public Rules getRules() {
        return validatedRules;
    }
}