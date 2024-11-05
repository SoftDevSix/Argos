package com.softdevsix.argos.repository;

import org.springframework.data.repository.CrudRepository;
import com.softdevsix.argos.domain.Project;

/** ProjectRepository */
public interface ProjectRepository extends CrudRepository<Project, Integer> {}
