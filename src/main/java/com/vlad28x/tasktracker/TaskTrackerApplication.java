package com.vlad28x.tasktracker;

import com.vlad28x.tasktracker.entity.Project;
import com.vlad28x.tasktracker.entity.enums.ProjectStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@SpringBootApplication
@EnableJpaAuditing
public class TaskTrackerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TaskTrackerApplication.class, args);
        EntityManagerFactory factory = (EntityManagerFactory) context.getBean("entityManagerFactory");
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(Project.builder().name("proj44").priority(2).status(ProjectStatus.ACTIVE).build());
        manager.getTransaction().commit();
    }

}
