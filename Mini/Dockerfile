# Use a base image
FROM openjdk:17-jre-slim

# Copy the JAR file into the container
COPY target/e-commerce.jar /e-commerce.jar

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/e-commerce.jar"]
