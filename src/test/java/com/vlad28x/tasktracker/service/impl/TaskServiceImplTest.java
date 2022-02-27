package com.vlad28x.tasktracker.service.impl;

import com.vlad28x.tasktracker.dto.TaskRequestDto;
import com.vlad28x.tasktracker.dto.TaskResponseDto;
import com.vlad28x.tasktracker.entity.Project;
import com.vlad28x.tasktracker.entity.Task;
import com.vlad28x.tasktracker.entity.enums.TaskStatus;
import com.vlad28x.tasktracker.exception.BadRequestException;
import com.vlad28x.tasktracker.exception.NotFoundException;
import com.vlad28x.tasktracker.repository.TaskRepository;
import com.vlad28x.tasktracker.util.mapper.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private ProjectServiceImpl projectService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    void getAllTasksSuccessTest() {
        List<Task> list = Stream.iterate(0, n -> n + 1)
                .limit(4)
                .map(a -> new Task())
                .collect(Collectors.toList());
        Mockito.when(taskRepository.findAll()).thenReturn(list);
        List<TaskResponseDto> excepted = taskService.getAll();
        assertEquals(excepted.size(), list.size());
    }

    @Test
    void getTaskByIdSuccessTest() {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task();
        task.setId(1L);
        task.setName("TASK-1: Create database");
        task.setDescription("Create database in PostgreSQL DBMS");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(1);
        Project project = new Project();
        project.setId(1L);
        task.setProject(project);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        Mockito.when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));

        TaskResponseDto excepted = TaskMapper.taskToTaskResponseDto(task);
        TaskResponseDto actual = taskService.getById(1L);

        assertEquals(excepted, actual);
    }

    @Test
    void getTaskByIdFailTest() {
        Mockito.when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.getById(1L));
    }

    @Test
    void createTaskSuccessTest() {
        LocalDateTime now = LocalDateTime.now();
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setId(2L);
        taskRequestDto.setName("TASK-2: Create database");
        taskRequestDto.setDescription("Create database in MySQL DBMS");
        taskRequestDto.setStatus(TaskStatus.TO_DO);
        taskRequestDto.setPriority(2);
        taskRequestDto.setProjectId(2L);

        Task task = TaskMapper.taskRequestDtoToTask(taskRequestDto);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto excepted = TaskMapper.taskToTaskResponseDto(task);
        TaskResponseDto actual = taskService.create(taskRequestDto);

        assertEquals(excepted, actual);
    }

    @Test
    void createTaskFailTest() {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(BadRequestException.class, () -> taskService.create(taskRequestDto));
    }

    @Test
    void updateTaskSuccessTest() {
        LocalDateTime now = LocalDateTime.now();
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setId(3L);
        taskRequestDto.setName("TASK-3: Create database");
        taskRequestDto.setDescription("Create database using MongoDB");
        taskRequestDto.setStatus(TaskStatus.DONE);
        taskRequestDto.setPriority(3);
        taskRequestDto.setProjectId(3L);

        Task task = TaskMapper.taskRequestDtoToTask(taskRequestDto);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        Mockito.when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto excepted = TaskMapper.taskToTaskResponseDto(task);
        TaskResponseDto actual = taskService.update(3L, taskRequestDto);

        assertEquals(excepted, actual);
    }

    @Test
    void updateTaskFailTest() {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        Mockito.when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.update(1L, taskRequestDto));
    }

    @Test
    void deleteTaskSuccessTest() {
        taskService.delete(1L);
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(Mockito.any(Long.class));
    }

    @Test
    void deleteTaskFailTest() {
        assertThrows(BadRequestException.class, () -> taskService.delete(null));
    }

    @Test
    void addTaskToProjectSuccessTest() {
        Project project = new Project();
        project.setId(1L);
        Task task = new Task();
        task.setId(1L);

        Mockito.when(projectService.findById(Mockito.any(Long.class))).thenReturn(project);
        Mockito.when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto excepted = TaskMapper.taskToTaskResponseDto(task);
        excepted.setProjectId(1L);
        TaskResponseDto actual = taskService.addTaskToProject(1L, 1L);

        assertEquals(excepted, actual);
    }

    @Test
    void addTaskToProjectFailTest() {
        Project project = new Project();
        project.setId(1L);
        Task task = new Task();
        task.setId(1L);
        task.setProject(project);

        Mockito.when(projectService.findById(Mockito.any(Long.class))).thenReturn(project);
        Mockito.when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));

        assertThrows(BadRequestException.class, () -> taskService.addTaskToProject(1L, 1L));
    }

    @Test
    void removeTaskFromProjectSuccessTest() {
        Project project = new Project();
        project.setId(1L);
        Task task = new Task();
        task.setId(1L);
        task.setProject(project);

        Mockito.when(projectService.findById(Mockito.any(Long.class))).thenReturn(project);
        Mockito.when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto excepted = TaskMapper.taskToTaskResponseDto(task);
        excepted.setProjectId(null);
        TaskResponseDto actual = taskService.removeTaskFromProject(1L, 1L);

        assertEquals(excepted, actual);
    }

    @Test
    void removeTaskFromProjectFailTest() {
        Project project = new Project();
        project.setId(1L);
        Task task = new Task();
        task.setId(1L);

        Mockito.when(projectService.findById(Mockito.any(Long.class))).thenReturn(project);
        Mockito.when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));

        assertThrows(BadRequestException.class, () -> taskService.removeTaskFromProject(1L, 1L));
    }

    @Test
    void getAllTasksFromProjectSuccessTest() {
        Project project = new Project();
        project.setId(1L);
        List<Task> list = Stream.iterate(0, n -> n + 1)
                .limit(4)
                .map(a -> new Task())
                .collect(Collectors.toList());

        Mockito.when(projectService.findById(Mockito.any(Long.class))).thenReturn(project);
        Mockito.when(taskRepository.findAllByProjectId(Mockito.any(Long.class))).thenReturn(list);

        List<TaskResponseDto> excepted = list.stream()
                .map(TaskMapper::taskToTaskResponseDto)
                .collect(Collectors.toList());
        List<TaskResponseDto> actual = taskService.getAllTasksFromProject(1L);

        assertEquals(excepted, actual);
    }

}