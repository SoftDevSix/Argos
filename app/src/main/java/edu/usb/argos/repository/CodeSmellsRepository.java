package edu.usb.argos.repository;

import edu.usb.argos.domain.CodeSmellsRules;
import org.springframework.data.repository.CrudRepository;

/**
 * CodeSmellsRepository
 */
public interface CodeSmellsRepository extends CrudRepository<CodeSmellsRules, Integer> {}
