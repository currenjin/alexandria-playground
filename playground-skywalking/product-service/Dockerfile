FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle bootJar --no-daemon

FROM apache/skywalking-java-agent:9.3.0-java17 AS agent

FROM eclipse-temurin:17-jre
COPY --from=agent /skywalking/agent /skywalking/agent
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-javaagent:/skywalking/agent/skywalking-agent.jar", "-jar", "app.jar"]
