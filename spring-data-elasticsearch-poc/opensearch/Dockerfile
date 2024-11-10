FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine

COPY ./build/libs/opensearch.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]