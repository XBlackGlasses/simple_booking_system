FROM openjdk:17-jdk

# copy .jar file
COPY ./target/bookService.jar app.jar

# Export the backend port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]