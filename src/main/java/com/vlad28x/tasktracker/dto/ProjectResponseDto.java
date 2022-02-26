package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.ProjectStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectResponseDto {

    private Long id;
    private String name;
    private ProjectStatus status;
    private Integer priority;
    private LocalDate startDate;
    private LocalDate completionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
