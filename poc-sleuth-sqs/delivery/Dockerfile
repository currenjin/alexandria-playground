FROM openjdk:8-jdk-alpine

COPY ./build/libs/delivery.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]