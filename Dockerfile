FROM openjdk:17
EXPOSE 5000
ADD Mini/target/e-commerce.jar e-commerce.jar
ENTRYPOINT ["java", "-jar", "/e-commerce.jar"]
