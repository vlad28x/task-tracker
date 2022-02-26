package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.TaskStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseDto {

    @ApiModelProperty(notes = "The database generated task ID")
    private Long id;
    @ApiModelProperty(notes = "The name of the task")
    private String name;
    @ApiModelProperty(notes = "The description of the task")
    private String description;
    @ApiModelProperty(notes = "The status of the task")
    private TaskStatus status;
    @ApiModelProperty(notes = "The priority of the task")
    private Integer priority;
    @ApiModelProperty(notes = "The project ID of the task")
    private Long projectId;
    @ApiModelProperty(notes = "The auto-generated date and time when the task was created")
    private LocalDateTime createdAt;
    @ApiModelProperty(notes = "The auto-generated date and time when the task was updated")
    private LocalDateTime updatedAt;

}
