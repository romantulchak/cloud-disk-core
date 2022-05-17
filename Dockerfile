FROM gradle:7.2-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM openjdk:11-jdk
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar cloud-disk.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "cloud-disk.jar"]
