package com.vlad28x.tasktracker.service;

import com.vlad28x.tasktracker.dto.ProjectRequestDto;
import com.vlad28x.tasktracker.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    List<ProjectResponseDto> getAll();

    ProjectResponseDto getById(Long id);

    ProjectResponseDto create(ProjectRequestDto newProject);

    ProjectResponseDto update(Long id, ProjectRequestDto newProject);

    void delete(Long id);

}
