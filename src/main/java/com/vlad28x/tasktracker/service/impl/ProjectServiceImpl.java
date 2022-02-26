package com.vlad28x.tasktracker.service.impl;

import com.vlad28x.tasktracker.dto.ProjectRequestDto;
import com.vlad28x.tasktracker.dto.ProjectResponseDto;
import com.vlad28x.tasktracker.exception.BadRequestException;
import com.vlad28x.tasktracker.exception.NotFoundException;
import com.vlad28x.tasktracker.repository.ProjectRepository;
import com.vlad28x.tasktracker.service.ProjectService;
import com.vlad28x.tasktracker.util.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
        if (id == null) {
            log.error("Project ID must not be null");
            throw new BadRequestException("Project ID must not be null");
        }
        return ProjectMapper.projectToProjectResponseDto(projectRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Project with ID %s is not found", id));
            return new NotFoundException(String.format("Project with ID %s is not found", id));
        }));
    }

    @Transactional
    @Override
    public ProjectResponseDto create(ProjectRequestDto newProject) {
        if (newProject == null) {
            log.error("Project must not be null");
            throw new BadRequestException("Project must not be null");
        }
        try {
            newProject.setId(null);
            return ProjectMapper.projectToProjectResponseDto(projectRepository.save(
                    ProjectMapper.projectRequestDroToProject(newProject)
            ));
        } catch (NestedRuntimeException e) {
            log.error(e.getMessage(), e.getCause());
            throw new BadRequestException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ProjectResponseDto update(ProjectRequestDto newProject) {
        if (newProject == null || newProject.getId() == null) {
            log.error("Project or ID must not be null");
            throw new BadRequestException("Project or ID must not be null");
        } else if (projectRepository.existsById(newProject.getId())) {
            try {
                return ProjectMapper.projectToProjectResponseDto(projectRepository.save(
                        ProjectMapper.projectRequestDroToProject(newProject)
                ));
            } catch (NestedRuntimeException e) {
                log.error(e.getMessage(), e.getCause());
                throw new BadRequestException(e.getMessage(), e.getCause());
            }
        } else {
            log.error(String.format("Project with ID %s is not found", newProject.getId()));
            throw new NotFoundException(String.format("Project with ID %s is not found", newProject.getId()));
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("Project ID must not be null");
            throw new BadRequestException("Project ID must not be null");
        }
        projectRepository.deleteById(id);
    }

}
