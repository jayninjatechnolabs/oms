FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/oms.jar /app/oms.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "oms.jar"]
