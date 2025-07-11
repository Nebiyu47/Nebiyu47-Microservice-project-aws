FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/inventory-service-*.jar app.jar
ENTRYPOINT ["java" ,"-jar" ,"app.jar"]