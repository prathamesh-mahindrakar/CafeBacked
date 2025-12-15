# Use an official Maven + JDK image to build the project
FROM maven:3.9.5-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml from TaxiServer folder
COPY pom.xml .

# Copy src folder from TaxiServer folder
COPY /src ./src

# Build the project and skip tests
RUN mvn clean package -DskipTests

# Use a minimal JDK runtime image for running the app
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the built jar from the Maven build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java","-jar","app.jar"]
