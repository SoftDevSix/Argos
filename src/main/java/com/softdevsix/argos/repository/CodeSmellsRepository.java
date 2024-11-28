package com.softdevsix.argos.repository;

import com.softdevsix.argos.domain.CodeSmellsRules;
import org.springframework.data.repository.CrudRepository;

/**
 * CodeSmellsRepository
 */
public interface CodeSmellsRepository extends CrudRepository<CodeSmellsRules, Integer> {}
