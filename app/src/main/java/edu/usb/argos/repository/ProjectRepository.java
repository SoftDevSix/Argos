package edu.usb.argos.repository;

import org.springframework.data.repository.CrudRepository;
import edu.usb.argos.domain.Project;

/** ProjectRepository */
public interface ProjectRepository extends CrudRepository<Project, Integer> {}
