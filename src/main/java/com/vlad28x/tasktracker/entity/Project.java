package com.vlad28x.tasktracker.entity;

import com.vlad28x.tasktracker.entity.enums.ProjectStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(exclude = "tasks", callSuper = true)
@EqualsAndHashCode(exclude = "tasks", callSuper = true)
public class Project extends AbstractEntity {

    private String name;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    private Integer priority;
    private LocalDate startDate;
    private LocalDate completionDate;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

}
