package com.vlad28x.tasktracker.service;

import com.vlad28x.tasktracker.dto.TaskRequestDto;
import com.vlad28x.tasktracker.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {

    List<TaskResponseDto> getAll();

    TaskResponseDto getById(Long id);

    TaskResponseDto create(TaskRequestDto newTask);

    TaskResponseDto update(Long id, TaskRequestDto newTask);

    void delete(Long id);

    TaskResponseDto addTaskToProject(Long projectId, Long taskId);

    TaskResponseDto removeTaskFromProject(Long projectId, Long taskId);

    List<TaskResponseDto> getAllTasksFromProject(Long projectId);

}
