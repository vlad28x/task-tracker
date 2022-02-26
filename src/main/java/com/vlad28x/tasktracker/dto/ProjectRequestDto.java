package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.ProjectStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectRequestDto {

    private Long id;
    private String name;
    private ProjectStatus status;
    private Integer priority;
    private LocalDate startDate;
    private LocalDate completionDate;

}
