package com.vlad28x.tasktracker.service.impl;

import com.vlad28x.tasktracker.dto.TaskRequestDto;
import com.vlad28x.tasktracker.dto.TaskResponseDto;
import com.vlad28x.tasktracker.entity.Project;
import com.vlad28x.tasktracker.entity.Task;
import com.vlad28x.tasktracker.exception.BadRequestException;
import com.vlad28x.tasktracker.exception.NotFoundException;
import com.vlad28x.tasktracker.repository.TaskRepository;
import com.vlad28x.tasktracker.service.TaskService;
import com.vlad28x.tasktracker.util.mapper.TaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final ProjectServiceImpl projectService;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectServiceImpl projectService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
    }


    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDto> getAll() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::taskToTaskResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public TaskResponseDto getById(Long id) {
        return TaskMapper.taskToTaskResponseDto(findById(id));
    }

    @Transactional
    @Override
    public TaskResponseDto create(TaskRequestDto newTask) {
        if (newTask == null) {
            log.error("The task must not be null");
            throw new BadRequestException("The task must not be null");
        }
        try {
            newTask.setId(null);
            return TaskMapper.taskToTaskResponseDto(taskRepository.save(
                    TaskMapper.taskRequestDtoToTask(newTask)
            ));
        } catch (NestedRuntimeException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public TaskResponseDto update(TaskRequestDto newTask) {
        if (newTask == null || newTask.getId() == null) {
            log.error("The task or ID must not be null");
            throw new BadRequestException("The task or ID must not be null");
        } else if (taskRepository.existsById(newTask.getId())) {
            try {
                return TaskMapper.taskToTaskResponseDto(taskRepository.save(
                        TaskMapper.taskRequestDtoToTask(newTask)
                ));
            } catch (NestedRuntimeException e) {
                log.error(e.getMessage(), e);
                throw new BadRequestException(e.getMessage(), e);
            }
        } else {
            log.error(String.format("The task with ID %s is not found", newTask.getId()));
            throw new NotFoundException(String.format("The task with ID %s is not found", newTask.getId()));
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("The task ID must not be null");
            throw new BadRequestException("The task ID must not be null");
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    @Override
    public TaskResponseDto addTaskToProject(Long projectId, Long taskId) {
        Project project = projectService.findById(projectId);
        Task task = findById(taskId);
        if (task.getProject() != null) {
            log.error(String.format("The task with ID %s already has a project with ID %s", taskId, task.getProject().getId()));
            throw new BadRequestException(String.format("The task with ID %s already has a project with ID %s", taskId, task.getProject().getId()));
        }
        task.setProject(project);
        taskRepository.addTaskToProject(projectId, taskId);
        return TaskMapper.taskToTaskResponseDto(task);
    }

    @Transactional
    @Override
    public TaskResponseDto removeTaskFromProject(Long projectId, Long taskId) {
        Project project = projectService.findById(projectId);
        Task task = findById(taskId);
        if (task.getProject() == null) {
            log.error(String.format("The task with ID %s already has not a project", taskId));
            throw new BadRequestException(String.format("The task with ID %s already has not a project", taskId));
        } else if (!task.getProject().equals(project)) {
            log.error(String.format("The task with ID %s is missing in the project with ID %s", taskId, projectId));
            throw new BadRequestException(String.format("The task with ID %s is missing in the project with ID %s", taskId, projectId));
        }
        task.setProject(null);
        taskRepository.removeTaskFromProject(taskId);
        return TaskMapper.taskToTaskResponseDto(task);
    }

    protected Task findById(Long id) {
        if (id == null) {
            log.error("The task ID must not be null");
            throw new BadRequestException("The task ID must not be null");
        }
        return taskRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("The task with ID %s is not found", id));
            return new NotFoundException(String.format("The task with ID %s is not found", id));
        });
    }

}
