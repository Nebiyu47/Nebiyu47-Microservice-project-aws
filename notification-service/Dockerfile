FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/notification-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]