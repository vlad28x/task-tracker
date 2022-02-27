# Task-Tracker

## Overview
Task tracker is an application for managing tasks of projects.
The application is developed using Spring Boot.

## Features

* Using Swagger for automated API documentation
* The logic level covers unit tests
* Ability to create / view / edit / delete information about projects
* Ability to create / view / edit / delete task information
* Ability to add and remove tasks from a project
* Ability to view all tasks in the project
* Ability to filter projects with various methods and by various fields

## Steps to Setup

**1. Clone the application**

```
git clone https://github.com/vlad28x/task-tracker.git
```

**2. Create PostgreSQL database**

```
create database postgres
```

**3. Update three configuration fields in application.properties inside /resources folder**

```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
```

**4. Run the application using Maven**

```
./mvnw clean install
./mvnw spring-boot:run
```

The app will start running at http://localhost:8080

## Dependencies

1. Spring Boot;
2. Swagger - for automated documentation;
3. Liquibase - for tracking, managing and applying database schema changes;
4. Lombok - for generating some boilered code;
5. JUnit 5 - for testing the application.

## To Do

- [ ] Fix bugs in the ProjectSpecification
- [ ] Setup Docker compose for deploy