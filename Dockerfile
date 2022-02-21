FROM openjdk:17-jdk-alpine

COPY target/image-server.jar image-server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/image-server.jar"]