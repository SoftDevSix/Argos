package com.softdevsix.argos.repository;

import java.util.Optional;

import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.domain.Rules;

/**
 * RulesRepo
 */
public interface RulesRepo {
  Integer createRule(Rules rules, Project project);
  Optional<Rules> fetchRule(Integer repoId);
}
