FROM maven:3.8.4-openjdk-17 as builder
MAINTAINER Felix Steinke <steinke.felix@yahoo.de>

WORKDIR /app
COPY  ./ .

RUN mvn clean install -DskipTests

FROM openjdk:17-alpine as runner
MAINTAINER Felix Steinke <steinke.felix@yahoo.com>

WORKDIR /home/app
COPY --from=builder /app/target/FreshPlanner-API-1.0.jar /home/app
RUN chgrp -R root /home/app && chmod -R 770 /home/app

EXPOSE 8080
ENTRYPOINT ["java","-jar", "./FreshPlanner-API-1.0.jar", "--spring.profiles.active=prod"]
