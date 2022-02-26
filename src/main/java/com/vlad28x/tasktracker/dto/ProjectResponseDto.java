package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.ProjectStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectResponseDto {

    @ApiModelProperty(notes = "The database generated project ID")
    private Long id;
    @ApiModelProperty(notes = "The name of the project")
    private String name;
    @ApiModelProperty(notes = "The status of the project")
    private ProjectStatus status;
    @ApiModelProperty(notes = "The priority of the project")
    private Integer priority;
    @ApiModelProperty(notes = "The start date of the project")
    private LocalDate startDate;
    @ApiModelProperty(notes = "The completion date of the project")
    private LocalDate completionDate;
    @ApiModelProperty(notes = "The auto-generated date and time when the project was created")
    private LocalDateTime createdAt;
    @ApiModelProperty(notes = "The auto-generated date and time when the project was updated")
    private LocalDateTime updatedAt;

}
