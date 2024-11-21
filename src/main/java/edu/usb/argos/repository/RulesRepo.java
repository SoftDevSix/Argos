package edu.usb.argos.argos.repository;

import java.util.Optional;

import edu.usb.argos.argos.domain.Project;
import edu.usb.argos.argos.domain.Rules;

/**
 * RulesRepo
 */
public interface RulesRepo {
  Integer createRule(Rules rules, Project project);
  Optional<Rules> fetchRule(Integer repoId);
}
