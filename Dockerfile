FROM eclipse-temurin:17-jre-alpine as builder
WORKDIR /workspace/extracted
ADD target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract && \
    rm app.jar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /workspace/application

COPY --from=builder /workspace/extracted/dependencies/ ./
COPY --from=builder /workspace/extracted/spring-boot-loader/ ./
COPY --from=builder /workspace/extracted/snapshot-dependencies/ ./
COPY --from=builder /workspace/extracted/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
