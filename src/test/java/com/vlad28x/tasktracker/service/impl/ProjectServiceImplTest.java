package com.vlad28x.tasktracker.service.impl;

import com.vlad28x.tasktracker.dto.ProjectRequestDto;
import com.vlad28x.tasktracker.dto.ProjectResponseDto;
import com.vlad28x.tasktracker.entity.Project;
import com.vlad28x.tasktracker.entity.enums.ProjectStatus;
import com.vlad28x.tasktracker.exception.BadRequestException;
import com.vlad28x.tasktracker.exception.NotFoundException;
import com.vlad28x.tasktracker.repository.ProjectRepository;
import com.vlad28x.tasktracker.util.mapper.ProjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Test
    void getAllProjectsSuccessTest() {
        List<Project> list = Stream.iterate(0, n -> n + 1)
                .limit(3)
                .map(a -> new Project())
                .collect(Collectors.toList());
        Mockito.when(projectRepository.findAll()).thenReturn(list);
        List<ProjectResponseDto> excepted = projectService.getAll();
        assertEquals(excepted.size(), list.size());
    }

    @Test
    void getProjectByIdSuccessTest() {
        LocalDateTime now = LocalDateTime.now();
        Project project = new Project();
        project.setId(1L);
        project.setName("Project Management System");
        project.setStatus(ProjectStatus.COMPLETED);
        project.setPriority(1);
        project.setStartDate(LocalDate.of(2022, 3, 1));
        project.setCompletionDate(LocalDate.of(2022, 9, 1));
        project.setCreatedAt(now);
        project.setUpdatedAt(now);

        Mockito.when(projectRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(project));

        ProjectResponseDto expected = ProjectMapper.projectToProjectResponseDto(project);
        ProjectResponseDto actual = projectService.getById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void getProjectByIdFailTest() {
        Mockito.when(projectRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> projectService.getById(1L));
    }

    @Test
    void createProjectSuccessTest() {
        LocalDateTime now = LocalDateTime.now();
        ProjectRequestDto projectRequestDto = new ProjectRequestDto();
        projectRequestDto.setId(1L);
        projectRequestDto.setName("Project Management System");
        projectRequestDto.setStatus(ProjectStatus.COMPLETED);
        projectRequestDto.setPriority(1);
        projectRequestDto.setStartDate(LocalDate.of(2022, 3, 1));
        projectRequestDto.setCompletionDate(LocalDate.of(2022, 9, 1));

        Project project = ProjectMapper.projectRequestDroToProject(projectRequestDto);
        project.setUpdatedAt(now);
        project.setCreatedAt(now);
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        ProjectResponseDto excepted = ProjectMapper.projectToProjectResponseDto(project);
        ProjectResponseDto actual = projectService.create(projectRequestDto);

        assertEquals(excepted, actual);
    }

    @Test
    void createProjectFailTest() {
        ProjectRequestDto projectRequestDto = new ProjectRequestDto();
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(BadRequestException.class, () -> projectService.create(projectRequestDto));
    }

    @Test
    void updateProjectSuccessTest() {
        LocalDateTime now = LocalDateTime.now();
        ProjectRequestDto projectRequestDto = new ProjectRequestDto();
        projectRequestDto.setId(1L);
        projectRequestDto.setName("Project Management System");
        projectRequestDto.setStatus(ProjectStatus.COMPLETED);
        projectRequestDto.setPriority(1);
        projectRequestDto.setStartDate(LocalDate.of(2022, 3, 1));
        projectRequestDto.setCompletionDate(LocalDate.of(2022, 9, 1));

        Project project = ProjectMapper.projectRequestDroToProject(projectRequestDto);
        project.setCreatedAt(now);
        project.setUpdatedAt(now);
        Mockito.when(projectRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(project));
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        ProjectResponseDto excepted = ProjectMapper.projectToProjectResponseDto(project);
        ProjectResponseDto actual = projectService.update(1L, projectRequestDto);

        assertEquals(excepted, actual);
    }

    @Test
    void updateProjectFailTest() {
        ProjectRequestDto projectRequestDto = new ProjectRequestDto();
        Mockito.when(projectRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> projectService.update(1L, projectRequestDto));
    }

    @Test
    void deleteProjectSuccessTest() {
        projectService.delete(1L);
        Mockito.verify(projectRepository, Mockito.times(1)).deleteById(Mockito.any(Long.class));
    }

    @Test
    void deleteProjectFailTest() {
        assertThrows(BadRequestException.class, () -> projectService.delete(null));
    }

}