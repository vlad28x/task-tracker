package com.vlad28x.tasktracker.service.impl;

import com.vlad28x.tasktracker.dto.FilterDto;
import com.vlad28x.tasktracker.dto.ProjectRequestDto;
import com.vlad28x.tasktracker.dto.ProjectResponseDto;
import com.vlad28x.tasktracker.entity.Project;
import com.vlad28x.tasktracker.exception.BadRequestException;
import com.vlad28x.tasktracker.exception.NotFoundException;
import com.vlad28x.tasktracker.repository.ProjectRepository;
import com.vlad28x.tasktracker.service.ProjectService;
import com.vlad28x.tasktracker.service.specification.ProjectSpecificationImpl;
import com.vlad28x.tasktracker.util.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final ProjectSpecificationImpl projectSpecification;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectSpecificationImpl projectSpecification) {
        this.projectRepository = projectRepository;
        this.projectSpecification = projectSpecification;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectResponseDto> getAll() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::projectToProjectResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ProjectResponseDto getById(Long id) {
        return ProjectMapper.projectToProjectResponseDto(findById(id));
    }

    @Transactional
    @Override
    public ProjectResponseDto create(ProjectRequestDto newProject) {
        if (newProject == null) {
            log.error("The project must not be null");
            throw new BadRequestException("The project must not be null");
        }
        try {
            newProject.setId(null);
            return ProjectMapper.projectToProjectResponseDto(projectRepository.save(
                    ProjectMapper.projectRequestDroToProject(newProject)
            ));
        } catch (NestedRuntimeException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public ProjectResponseDto update(Long id, ProjectRequestDto newProject) {
        if (newProject == null) {
            log.error("The project must not be null");
            throw new BadRequestException("The project must not be null");
        }
        Project project = findById(id);
        setPropertiesFromProjectRequestDtoToProject(project, newProject);
        return ProjectMapper.projectToProjectResponseDto(projectRepository.save(project));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("The project ID must not be null");
            throw new BadRequestException("The project ID must not be null");
        }
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectResponseDto> getFilteredProjects(List<FilterDto> filters) {
        List<Project> list = null;
        try {
            list = projectRepository.findAll(projectSpecification.getSpecificationFromFilters(filters));
        } catch (NestedRuntimeException | DateTimeParseException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
        return list.stream()
                .map(ProjectMapper::projectToProjectResponseDto)
                .collect(Collectors.toList());
    }

    protected Project findById(Long id) {
        if (id == null) {
            log.error("The project ID must not be null");
            throw new BadRequestException("The project ID must not be null");
        }
        return projectRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("The project with ID %s is not found", id));
            return new NotFoundException(String.format("The project with ID %s is not found", id));
        });
    }

    protected void setPropertiesFromProjectRequestDtoToProject(Project project, ProjectRequestDto dto) {
        if (dto.getName() != null) project.setName(dto.getName());
        if (dto.getStatus() != null) project.setStatus(dto.getStatus());
        if (dto.getPriority() != null) project.setPriority(dto.getPriority());
        project.setStartDate(dto.getStartDate());
        project.setCompletionDate(dto.getCompletionDate());
    }

}
