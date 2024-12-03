package edu.usb.argos.repository;

import java.util.Optional;

import edu.usb.argos.domain.Project;
import edu.usb.argos.domain.Rules;

/**
 * RulesRepo
 */
public interface RulesRepo {
  Integer createRule(Rules rules, Project project);
  Optional<Rules> fetchRule(Integer repoId);
}
