# 서비스별 이미지 (ARG MODULE로 파라미터화). docker-compose가 각 서비스에 MODULE 지정.
# MODULE은 디렉토리 경로(예: services/oms). Gradle 프로젝트 경로는 '/'→':' 치환(:services:oms).
# 멀티스테이지: gradle로 해당 모듈 bootJar 빌드 → JRE 런타임.
FROM gradle:8.11-jdk21 AS build
WORKDIR /src
COPY . .
ARG MODULE
RUN gradle ":$(echo ${MODULE} | tr '/' ':'):bootJar" --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
ARG MODULE
COPY --from=build /src/${MODULE}/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
