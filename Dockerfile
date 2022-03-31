FROM maven:3.8.4-openjdk-17 as builder
MAINTAINER Felix Steinke <https://github.com/felixsteinke>

WORKDIR /app
COPY  ./ .

RUN mvn clean install -DskipTests

FROM openjdk:17-alpine as runner
MAINTAINER Felix Steinke <https://github.com/felixsteinke>

WORKDIR /home/app
COPY --from=builder /app/target/FreshPlanner-API-1.0.jar /home/app
RUN chgrp -R root /home/app && chmod -R 770 /home/app

EXPOSE 8080
ENTRYPOINT ["java","-jar", "./FreshPlanner-API-1.0.jar", "--spring.profiles.active=prod"]
