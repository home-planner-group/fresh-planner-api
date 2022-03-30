[![Continuous-Integration](https://github.com/FoodAppGroup/FreshPlanner-API/actions/workflows/ci.yml/badge.svg)](https://github.com/FoodAppGroup/FreshPlanner-API/actions/workflows/ci.yml)

# FreshPlanner API

This project was generated with [Spring Initializer](https://start.spring.io/) version 2.5.10.

The application starts on [http://localhost:8080](http://localhost:8080).

## Architecture

### Overview

```
         Http Entrypoint <-- Security Filter
                |                   |
Models -->  Controller   <-- Configuration <-- Main
   |            |
   |        Database
   |            |
 Entity --> Repository
                |
            MySQL DBMS
```

### Explanation

* Http Entrypoint = `localhost:8080/request-path`
* Security Filter = [security-package](src/main/java/com/freshplanner/api/security)
* Configuration = [configuration-package](src/main/java/com/freshplanner/api/configuration)
* Main = [Main Class](src/main/java/com/freshplanner/api/Application.java)
* Controller = [controller-package](src/main/java/com/freshplanner/api/controller)
* Models = [model-package](src/main/java/com/freshplanner/api/model)
* Database = [DB-classes in database package](src/main/java/com/freshplanner/api/database)
* Repository = [Repository-classes in database package](src/main/java/com/freshplanner/api/database)
* Entity = [Entity-classes in database package](src/main/java/com/freshplanner/api/database)
* MySQL DBMS = `mysql://localhost:3306`

## Dev Requirements

* Download and Install [Java Development Kit](https://www.oracle.com/java/technologies/downloads/#jdk17) v17.0+
    * Add `JAVA_HOME`
    * Update `PATH`
* Download and Install [Maven](https://maven.apache.org/download.cgi) v3.8.4+
    * Add `MAVEN_HOME`
    * Update `PATH`
* Download and Install [MySQL Server](https://dev.mysql.com/downloads/installer/) v8.0+
    * Check [application.properties](src/main/resources/application.properties) for correct configuration
* IDEA Configuration for Spring Dev Tools
    * The Spring Dev Tools enable __hot-swap__ the make development faster. When a __new Build__ gets started, it
      automatically restarts the application. To make the best use of it, activate following settings:
        * `Settings > Build, Execution, Deployment > Compiler > Build project automatically`
        * `Registry > compiler.automake.allow.when.app.running`
* Download and Install [Docker](https://docs.docker.com/desktop/windows/install/)
    * Build Image: `docker build -t fresh-planner-api .`

## [Dependencies](pom.xml)

* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.4/reference/htmlsingle/#boot-features-developing-web-applications)
    * Guide Web Service: [Building a REST Web Service](https://spring.io/guides/gs/rest-service/)
* Lombok
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.4/reference/htmlsingle/#using-boot-devtools)
* SpringFox Swagger UI
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.4/reference/htmlsingle/#boot-features-jpa-and-spring-data)
    * Guide JPA: [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
    * Guide MySQL: [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* Spring Security with JWT
* Spring Boot Validation
* [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/docs/2.6.4/maven-plugin/reference/html/)

## GitHub Workflows

### [Continuous Integration](.github/workflows/ci.yml)

* Executes __Build__
* Run __Tests__ with MySQL DB
* Perform __CodeQL__ Analysis with Java

## [Docker Image](Dockerfile)

* Divided into __Builder__ and __Runner__
* Image with [Alpine JDK](https://hub.docker.com/_/openjdk) and the executable `JAR`
* Exposes `Port 8080`
* Uses by default __MySQL DB__ at `Port 3306`
* Detailed configuration: [prod.properties](src/main/resources/application-prod.properties)

