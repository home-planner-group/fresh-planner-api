[![Continuous-Integration](https://github.com/home-planner-group/fresh-planner-api/actions/workflows/ci.yml/badge.svg)](https://github.com/home-planner-group/fresh-planner-api/actions/workflows/ci.yml)
[![Docker-Image](https://github.com/home-planner-group/fresh-planner-api/actions/workflows/docker-image.yml/badge.svg)](https://github.com/home-planner-group/fresh-planner-api/actions/workflows/docker-image.yml)

# Fresh Planner API

This project was generated with [Spring Initializer](https://start.spring.io/) version 2.5.10.

The application starts on [http://localhost:8080](http://localhost:8080).

### Purpose

This project has the purpose to get involved with __Spring Boot__ (Java, Maven), __Docker__ and __GitHub__ (Actions,
Packages, Projects).

### Description

The application is a __Spring Boot REST API__ that provides features to manage data in a MySQL database. The data
includes several entities that are related to the weekly food shopping and planning the meals. The basic features are
all about managing the data first and provide the correct connections between the entities. The advances features are
operations between the entities, for example you plan a recipe for the next week and add it with this operation to your
shopping cart.

## Architecture

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

<details>
  <summary>Explanations</summary>

* Http Entrypoint = `localhost:8080/request-path`
* Security Filter = [security-package](src/main/java/com/freshplanner/api/security)
* Configuration = [configuration-package](src/main/java/com/freshplanner/api/configuration)
* Main = [Main Class](src/main/java/com/freshplanner/api/Application.java)
* Controller = [controller-package](src/main/java/com/freshplanner/api/controller)
* Models = [model-package](src/main/java/com/freshplanner/api/model)
* Database = [DB-classes in database package](src/main/java/com/freshplanner/api/service)
* Repository = [Repository-classes in database package](src/main/java/com/freshplanner/api/service)
* Entity = [Entity-classes in database package](src/main/java/com/freshplanner/api/service)
* MySQL DBMS = `mysql://localhost:3306`

</details>

## GitHub Workflows & Docker Image

<details>
  <summary>Dockerfile</summary>

* [Dockerfile](Dockerfile)
  * Multistage Build: __Builder__ & __Runner__
  * Image with [Alpine JDK](https://hub.docker.com/_/openjdk) and the executable `JAR`
  * Exposes `Port 8080`
  * Uses by default __MySQL DB__ at `Port 3306`
  * Detailed configuration: [prod.properties](src/main/resources/application-prod.properties)

</details>

<details>
  <summary>Continuous Integration Workflow</summary>

* [.github/workflows/ci.yml](.github/workflows/ci.yml)
  * __Trigger:__ all pushes
  * Executes `mvn install`
  * Run `mvn test` with MySQL DB
  * Perform __CodeQL__ Analysis with Java

</details>

<details>
  <summary>Docker Image Delivery Workflow</summary>

* [.github/workflows/docker-image.yml](.github/workflows/docker-image.yml)
  * __Trigger:__ manual or on published release
  * Executes `docker build`
  * Execute `docker push` to GitHub Packages

</details>

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
