FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app


COPY target/bank-kata-*.jar bank-kata.jar


EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "bank-kata.jar"]