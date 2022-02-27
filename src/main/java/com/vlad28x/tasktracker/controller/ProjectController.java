package com.vlad28x.tasktracker.controller;

import com.vlad28x.tasktracker.dto.ProjectRequestDto;
import com.vlad28x.tasktracker.dto.ProjectResponseDto;
import com.vlad28x.tasktracker.dto.TaskResponseDto;
import com.vlad28x.tasktracker.service.ProjectService;
import com.vlad28x.tasktracker.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping
    @ApiOperation("View the list of projects")
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @GetMapping("/{id}")
    @ApiOperation("Search the project with an ID")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    @PostMapping
    @ApiOperation("Add the project")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto newProject) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(newProject));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update the project")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDto newProject) {
        return ResponseEntity.ok(projectService.update(id, newProject));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete the project")
    public void deleteProject(@PathVariable Long id) {
        projectService.delete(id);
    }

    @PutMapping("/{projectId}/add/{taskId}")
    @ApiOperation("Add the task to the project")
    public ResponseEntity<TaskResponseDto> addTaskToProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.addTaskToProject(projectId, taskId));
    }

    @PutMapping("/{projectId}/remove/{taskId}")
    @ApiOperation("Remove the task from the project")
    public ResponseEntity<TaskResponseDto> removeTaskFromProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.removeTaskFromProject(projectId, taskId));
    }

    @GetMapping("/{projectId}/tasks")
    @ApiOperation("View the list of tasks from a specific project")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksFromProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getAllTasksFromProject(projectId));
    }

}
