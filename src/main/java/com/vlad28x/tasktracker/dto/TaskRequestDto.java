package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskRequestDto {

    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private Integer priority;
    private Long projectId;

}
