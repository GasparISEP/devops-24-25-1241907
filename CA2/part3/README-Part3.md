# CA2: Part3 - Containers with Docker

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

## Table of Contents

- [Introduction](#introduction)
- [Environment Setup](#environment-setup)
- [Dockerfile - version 1](#dockerfile---version-1)
- [Dockerfile - version 2](#dockerfile---version-2)
- [Conclusion](#conclusion)


## Introduction

The main objective of this assignment is to gain hands-on experience with Docker by building Docker images and deploying containers for a chat application.
This chat server was initially created during the CA1 project.
By containerizing the application, we aim to ensure consistent behavior across various environments.
The assignment is structured into two parts:
- Building the chat server "inside" the Dockerfile.
- Building the chat server on the host computer and then copying the JAR file into the Dockerfile.
  In this report, I document the steps I took to achieve these objectives, including the environment setup, the content
  of the Dockerfiles, and the process of building and running the Docker images.

## Environment Setup

To begin working with Docker and the chat server developed during CA1, I first made sure that Docker was properly installed on my machine.

```bash
  docker --version
```

Additionally, it’s essential to have access to the chat server repository hosted on Bitbucket.
This repository holds the basic Gradle-based application created in CA1.

```bash
  git clone https://bitbucket.org/pssmatos/gradle_basic_demo/
```

## Dockerfile - version 1

Below are the steps I followed to set up and launch the chat server within a Docker container:

1. I verified that Docker was up and running on my system.

2. I moved to the directory containing the Dockerfile.

3. The Dockerfile I used had the following content:

```dockerfile
# Build stage using Gradle and JDK 21
FROM gradle:8.5.0-jdk21 AS builder

# Set the working directory for the build
WORKDIR /app

# Copy all project files into the container
COPY . .

# Make the Gradle wrapper executable and build the JAR
RUN chmod +x gradlew \
    && ./gradlew build --no-daemon

# Final stage: use a lightweight JRE image for running the application
FROM eclipse-temurin:21-jre

# Set the working directory for the runtime container
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/basic_demo-0.1.0.jar ./basic_demo-0.1.0.jar

# Expose the port used by the chat server
EXPOSE 59001

# Set the entry point to run the chat server
ENTRYPOINT ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
```

The Dockerfile is a script that defines a sequence of steps to build a Docker image for the chat server.
It begins by using a Gradle image with JDK 21 to compile the project from the local source code. After the build process is finished, it transitions to a lightweight JRE image to produce a smaller and more efficient container suitable for production.
The generated JAR file is transferred from the build stage to the final image, and the server is configured to listen on port 59001.

4. I created the Docker image by running the following command:

```bash
  docker build -f DockerfileV1 -t gasparfigueiras/chat-server:version1 .
```

The -t flag is used to assign a name and version tag to the image. In this case, the image is tagged as gasparfigueiras/chat-server:version1.

5. To verify that the image was successfully built, I executed the following command:

```bash
  docker images
```
<img src="https://i.imgur.com/s9rFNeu.png" alt="Docker images" width="600"/>


6. I started the Docker container by running the following command:

```bash
  docker run -p 59001:59001 gasparfigueiras/chat-server:version1
```

The -p flag is used to link a port on the host machine to a port inside the container.
In this case, port 59001 on the host is mapped to port 59001 within the container.
Shown below is the output of the command, indicating that the Docker container is running the chat server:

<img src="https://i.imgur.com/RP2izNp.png" alt="Docker run" width="600">

7. In a separate terminal window, I navigated to the directory containing the chat client and executed the following commands to build and launch it:

```bash
  ./gradlew build
  ./gradlew runClient
```

8. I connected two separate clients to the chat server in order to test its functionality. 
Below is the output from one of the clients interacting with the chat server running inside the Docker container, displaying a sample message:

<img src="https://i.imgur.com/qKLCZLb.png" alt="Chat Client1" width="500">
<img src="https://i.imgur.com/ddyw3bE.png" alt="Chat Client2" width="500">

In the terminal where the Docker container was running, I was able to observe clients joining and leaving the chat server.

<img src="https://i.imgur.com/whXbQql.png" alt="Chat Server" width="1000">

9. Finally, I uploaded the Docker image to Docker Hub using the following command:

```bash
  docker push gasparfigueiras/chat-server:version1
```

The image was successfully pushed to the Docker Hub repository and is now available for use, as illustrated in the screenshot below:

<img src="https://i.imgur.com/cXYeco1.png" alt="Docker Hub image" width="1000">

## Dockerfile - version 2

1. I began by navigating to the project directory and executing the Gradle build command to generate the JAR file:

```bash
  ./gradlew build
```

This process produced the basic_demo-0.1.0.jar file inside the build/libs directory.

2. Next, I navigated to the directory containing the Dockerfile for version 2 of the application.

3. Below is the content of the Dockerfile that I used for this version:

```dockerfile
# Use a lightweight JRE image based on Eclipse Temurin with Java 21
FROM eclipse-temurin:21-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host to the container's working directory
COPY build/libs/basic_demo-0.1.0.jar ./basic_demo-0.1.0.jar

# Expose the port the chat server will use
EXPOSE 59001

# Define the entry point to launch the chat server
ENTRYPOINT ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
```

This Dockerfile is more straightforward than the previous one, as it doesn’t require cloning the repository or building the project.
Instead, it copies the JAR file—previously generated by the Gradle build on the host machine—into the Docker image and sets up the server to run on port 59001.

4. I created the Docker image by executing the following command:

```bash
   docker build -f DockerfileV2 -t gasparfigueiras/chat-server:version2 .
```

The -t flag assigns a name and version tag to the image. In this case, the image is tagged as gasparfigueiras/chat-server:version2.
The -f flag specifies the Dockerfile to be used—in this case, DockerfileV2.
The . at the end sets the build context to the current directory, ensuring that all required files are available during the build process.

5. To confirm that the image was successfully built, I executed the following command:

```bash
  docker images
```

The output of the command below confirms that the Docker image was successfully created:

<img src="https://i.imgur.com/95GHX1z.png" alt="Docker Hub image" width="600">

6. I started the Docker container by executing the following command:

```bash
  docker run -p 59001:59001 gasparfigueiras/chat-server:version2
```

The output below shows the Docker container successfully running the chat server:

<img src="https://i.imgur.com/gaHIpSh.png" alt="Docker Hub image" width="1000">

7. I navigated to the directory containing the chat client and executed the following commands to launch it:

```bash
  ./gradlew runClient
```

I launched two client instances in separate terminal windows to test the chat functionality.
Shown below is the output of the conversation:

<img src="https://i.imgur.com/zixAchu.png" alt="Chat Clients" width="1000"/>

In the terminal where the Docker container was active, I could see messages indicating when clients joined or left the chat:

<img src="https://i.imgur.com/5woZpyt.png" alt="Chat Server" width="1000"/>

8. Finally, I uploaded the Docker image to Docker Hub using the following command:

```bash
  docker push gasparfigueiras/chat-server:version2
```

The image was successfully uploaded to the Docker Hub repository and is now available for use, as illustrated in the screenshot below:

<img src="https://i.imgur.com/KVRRgqg.png" alt="Docker Hub image" width="1000"/>

## Conclusion

In this assignment, I successfully containerized a chat server application using Docker.
By following a structured set of steps, I created two distinct versions of the Docker image.
The first version handled the build process within the Dockerfile itself, while the second version used a pre-built JAR file from the host machine.
Both methods highlighted Docker’s flexibility and effectiveness in ensuring consistent deployment across different environments.