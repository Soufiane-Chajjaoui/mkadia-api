FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/mkadia-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
