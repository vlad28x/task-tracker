package com.vlad28x.tasktracker.entity;

import com.vlad28x.tasktracker.entity.enums.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Task extends AbstractEntity {

    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private Integer priority;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

}
