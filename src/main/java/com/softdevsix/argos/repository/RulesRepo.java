package com.softdevsix.argos.repository;

import com.softdevsix.argos.domain.Rules;

/**
 * RulesRepo
 */
public interface RulesRepo {
  void createRule(Rules rules);
  Rules fetchRule(Integer repoId);
}
