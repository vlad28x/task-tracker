package com.vlad28x.tasktracker.controller;

import com.vlad28x.tasktracker.dto.TaskRequestDto;
import com.vlad28x.tasktracker.dto.TaskResponseDto;
import com.vlad28x.tasktracker.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @ApiOperation("View the list of tasks")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @GetMapping("/{id}")
    @ApiOperation("Search the task with an ID")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PostMapping
    @ApiOperation("Add the task")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto newTask) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(newTask));
    }

    @PatchMapping("/{id}")
    @ApiOperation("Update the task")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @RequestBody TaskRequestDto newTask) {
        return ResponseEntity.ok(taskService.update(id, newTask));
    }

    @ApiOperation("Delete the task")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }

}
