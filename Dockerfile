# Use the official Ubuntu image as the base image
FROM ubuntu:latest

# Set the maintainer label
LABEL maintainer="yourname@example.com"

# Install required packages
RUN apt-get update && \
    apt-get install -y \
    openjdk-21-jdk \
    git \
    curl \
    && apt-get clean

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper files into the container
COPY .mvn/ .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml

# Copy the rest of the application source code into the container
COPY . /app

# Grant execution permissions for the Maven wrapper
RUN chmod +x mvnw

# Run Maven clean install
RUN ./mvnw clean install

# Define the command to run the application (optional)
# CMD ["java", "-jar", "target/your-app.jar"]

# Expose the application's port (if applicable)
# EXPOSE 8080