package com.softdevsix.argos.repository;

import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.domain.Rules;

/**
 * RulesRepo
 */
public interface RulesRepo {
  Integer createRule(Rules rules, Project project);
  Rules fetchRule(Integer repoId);
}
