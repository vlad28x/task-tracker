package com.vlad28x.tasktracker.util.mapper;

import com.vlad28x.tasktracker.dto.ProjectRequestDto;
import com.vlad28x.tasktracker.dto.ProjectResponseDto;
import com.vlad28x.tasktracker.entity.Project;

public final class ProjectMapper {

    private ProjectMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Project projectRequestDroToProject(ProjectRequestDto dto) {
        Project project = new Project();
        project.setId(dto.getId());
        project.setName(dto.getName());
        project.setStatus(dto.getStatus());
        project.setPriority(dto.getPriority());
        project.setStartDate(dto.getStartDate());
        project.setCompletionDate(dto.getCompletionDate());
        return project;
    }

    public static ProjectResponseDto projectToProjectResponseDto(Project project) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setStatus(project.getStatus());
        dto.setPriority(project.getPriority());
        dto.setStartDate(project.getStartDate());
        dto.setCompletionDate(project.getCompletionDate());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }

}
