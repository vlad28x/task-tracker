package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseDto {

    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private Integer priority;
    private Long projectId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
