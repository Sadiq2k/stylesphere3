FROM openjdk:17
EXPOSE 5000
ADD target/e-commerce.jar e-commerce.jar.jar
ENTRYPOINT ["java","-jar","/e-commerce.jar"]