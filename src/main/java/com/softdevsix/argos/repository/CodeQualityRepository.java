package com.softdevsix.argos.repository;

import com.softdevsix.argos.domain.CodeQualityRules;
import org.springframework.data.repository.CrudRepository;

/**
 * CodeQualityRepository
 */
public interface CodeQualityRepository extends CrudRepository<CodeQualityRules, Integer> {}
