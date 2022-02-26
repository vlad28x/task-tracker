package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.TaskStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskRequestDto {

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

}
