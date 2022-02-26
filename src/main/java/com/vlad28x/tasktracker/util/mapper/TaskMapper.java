package com.vlad28x.tasktracker.util.mapper;

import com.vlad28x.tasktracker.dto.TaskRequestDto;
import com.vlad28x.tasktracker.dto.TaskResponseDto;
import com.vlad28x.tasktracker.entity.Project;
import com.vlad28x.tasktracker.entity.Task;

public final class TaskMapper {

    private TaskMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Task taskRequestDtoToTask(TaskRequestDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        if (dto.getProjectId() != null) {
            Project project = new Project();
            project.setId(dto.getProjectId());
            task.setProject(project);
        }
        return task;
    }

    public static TaskResponseDto taskToTaskResponseDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
        }
        return dto;
    }

}
