FROM openjdk:21-jdk
RUN groupadd -r spring
RUN useradd -r -g spring spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} christmas-backend.jar
ENTRYPOINT ["java","-jar","/christmas-backend.jar"]
