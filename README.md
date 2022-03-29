# FreshPlanner API

This project was generated with [Spring Initializer](https://start.spring.io/) version 2.5.10.

The application start on [http://localhost:8080](http://localhost:8080).

## Dev Requirements

* Download and Install [Java Development Kit](https://www.oracle.com/java/technologies/downloads/#jdk17) v17.0+
    * Add `JAVA_HOME`
    * Update `PATH`
* Download and Install [Maven](https://maven.apache.org/download.cgi) v3.8.4+
    * Add `MAVEN_HOME`
    * Update `PATH`
* Download and Install [MySQL Server](https://dev.mysql.com/downloads/installer/) v8.0+
* IDEA Configuration for Spring Dev Tools
    * The Spring Dev Tools enable __hot-swap__ the make development faster. When a __new Build__ gets started, it
      automatically restarts the application. To make the best use of it, activate following settings:
        * `Settings > Build, Execution, Deployment > Compiler > Build project automatically`
        * `Registry > compiler.automake.allow.when.app.running`
* Download and Install [Docker](https://docs.docker.com/desktop/windows/install/)
    * Build: `docker build -t fresh-planner-api .`

## Dependencies

Read [pom.xml](pom.xml) and have a look into [application.properties](src/main/resources/application.properties).

## Spring References

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.4/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.4/reference/htmlsingle/#using-boot-devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.4/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.4/reference/htmlsingle/#boot-features-jpa-and-spring-data)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a REST Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
