package com.vlad28x.tasktracker.service.impl;

import com.vlad28x.tasktracker.dto.TaskRequestDto;
import com.vlad28x.tasktracker.dto.TaskResponseDto;
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

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
        if (id == null) {
            log.error("Task ID must not be null");
            throw new BadRequestException("Task ID must not be null");
        }
        return TaskMapper.taskToTaskResponseDto(taskRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Task with ID %s is not found", id));
            return new NotFoundException(String.format("Task with ID %s isn not found", id));
        }));
    }

    @Transactional
    @Override
    public TaskResponseDto create(TaskRequestDto newTask) {
        if (newTask == null) {
            log.error("Task must not be null");
            throw new BadRequestException("Task must not be null");
        }
        try {
            newTask.setId(null);
            return TaskMapper.taskToTaskResponseDto(taskRepository.save(
                    TaskMapper.taskRequestDtoToTask(newTask)
            ));
        } catch (NestedRuntimeException e) {
            log.error(e.getMessage(), e.getCause());
            throw new BadRequestException(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    @Override
    public TaskResponseDto update(TaskRequestDto newTask) {
        if (newTask == null || newTask.getId() == null) {
            log.error("Task or ID must not be null");
            throw new BadRequestException("Task or ID must not be null");
        } else if (taskRepository.existsById(newTask.getId())) {
            try {
                return TaskMapper.taskToTaskResponseDto(taskRepository.save(
                        TaskMapper.taskRequestDtoToTask(newTask)
                ));
            } catch (NestedRuntimeException e) {
                log.error(e.getMessage(), e.getCause());
                throw new BadRequestException(e.getMessage(), e.getCause());
            }
        } else {
            log.error(String.format("Task with ID %s is not found", newTask.getId()));
            throw new NotFoundException(String.format("Task with ID %s is not found", newTask.getId()));
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("Task ID must not be null");
            throw new BadRequestException("Task ID must not be null");
        }
        taskRepository.deleteById(id);
    }

}
