FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -Dspotless.check.skip=true

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/sas.dhop.site.be-0.0.1-SNAPSHOT.jar /app/backend.jar
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["java", "-jar", "-Dserver.port=${PORT}", "/app/backend.jar"]