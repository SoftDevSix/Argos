package edu.usb.argos.service;

import edu.usb.argos.domain.Project;
import edu.usb.argos.domain.ProjectRules;
import edu.usb.argos.exception.ProjectNotFoundException;
import edu.usb.argos.repository.ProjectRepository;
import edu.usb.argos.repository.ProjectRulesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProjectValidatorTests {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectRulesRepository projectRulesRepository;

    @InjectMocks
    private ProjectValidator projectValidator;

    private Project mockProject;
    private Integer projectId = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockProject = new Project();
        mockProject.setId(projectId);
    }

    @Test
    void testValidate_withValidProject() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(projectRulesRepository.findAll()).thenReturn(Collections.emptyList());

        Project result = projectValidator.validate(projectId);

        assertNotNull(result, "Project should not be null");
        assertEquals(projectId, result.getId(), "Project ID should match");
    }

    @Test
    void testValidate_withExistingProjectRules() {
        ProjectRules projectRules = ProjectRules.builder()
            .project(mockProject)
            .build();
        when(projectRulesRepository.findById(projectId)).thenReturn(Optional.of(projectRules));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectValidator.validate(projectId);
        });

        assertEquals("Project already has rules", exception.getMessage());
    }

    @Test
    void testValidate_withNonexistentProject() {

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
        when(projectRulesRepository.findAll()).thenReturn(Collections.emptyList());

        ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class, () -> {
            projectValidator.validate(projectId);
        });

        assertEquals("Provided project ID is not registered", exception.getMessage());
    }
}
