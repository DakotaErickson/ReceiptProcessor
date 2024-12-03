FROM openjdk:17-oracle

WORKDIR app

# Copy the JAR file to the working directory
COPY target/*.jar app.jar

EXPOSE 9000

CMD ["java", "-jar", "app.jar"]