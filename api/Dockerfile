FROM amazoncorretto:21-alpine-jdk
RUN addgroup -S spring
RUN adduser -S spring -G spring 
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} christmas-backend.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar","/christmas-backend.jar"]
