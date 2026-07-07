# 서비스별 이미지 (ARG MODULE로 파라미터화). docker-compose가 각 서비스에 MODULE 지정.
# 멀티스테이지: gradle로 해당 모듈 bootJar 빌드 → JRE 런타임.
FROM gradle:8.11-jdk21 AS build
WORKDIR /src
COPY . .
ARG MODULE
RUN gradle :${MODULE}:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
ARG MODULE
COPY --from=build /src/${MODULE}/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
