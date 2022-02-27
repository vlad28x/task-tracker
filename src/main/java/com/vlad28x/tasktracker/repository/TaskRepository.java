package com.vlad28x.tasktracker.repository;

import com.vlad28x.tasktracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query("update Task t set t.project.id = :projectId where t.id = :taskId")
    Integer addTaskToProject(Long projectId, Long taskId);

    @Modifying
    @Query("update Task t set t.project.id = null where t.id = :taskId")
    Integer removeTaskFromProject(Long taskId);

    List<Task> findAllByProjectId(Long projectId);

}
