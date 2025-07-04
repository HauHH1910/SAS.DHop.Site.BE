FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/sas.dhop.site.be-0.0.1-SNAPSHOT.jar /app/backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]