# CA2: Part 4 - Containers with Docker

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

## Table of Contents

- [Introduction](#introduction)
- [DB Dockerfile](#db-dockerfile)
- [Web Dockerfile](#web-dockerfile)
- [Docker Compose](#docker-compose)
- [Tag and Push Images](#tag-and-push-images)
- [Working with volumes](#working-with-volumes)
- [Alternative solution](#alternative-solution)2
- [Conclusion](#conclusion)

## Introduction

This report outlines the journey of containerizing a web application using Docker.
The primary objective was to illustrate how to build, deploy, and manage both a web app and its accompanying database within isolated Docker containers.

The report details the development of Dockerfiles for the web application and the database, the configuration of Docker Compose to coordinate service interactions, and the process of tagging and pushing images to a remote container registry.
Through this hands-on experience, I enhanced my understanding of container-based deployments and contemporary DevOps practices.

As part of the exploration, I also evaluated Heroku—a cloud platform known for streamlining app deployment and management—as an alternative hosting solution.


## DB Dockerfile

I began by setting up a `Dockerfile` for the database service, which was based on the H2 database server.
This Dockerfile was located inside a `db` directory and contained the following configuration:

```dockerfile
FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y openjdk-11-jdk-headless && \
    apt-get install unzip -y && \
    apt-get install wget -y

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app/

RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar

EXPOSE 8082
EXPOSE 9092

CMD ["java", "-cp", "./h2-1.4.200.jar", "org.h2.tools.Server", "-web", "-webAllowOthers", "-tcp", "-tcpAllowOthers", "-ifNotExists"]
```

##### Explanation:

- **Base Image**: The image starts from ubuntu:latest, providing a clean and up-to-date environment.
- **Install Java**: OpenJDK 11 is installed to support the Java runtime required by the H2 database.
Utilities like unzip and wget are also included.
- **Directory Setup**: The /usr/src/app directory is created to store application-related files.
- **Download H2 Database**: The H2 database JAR file is fetched directly from the Maven central repository.
- **Port Configure**: Ports 8082 (for web interface) and 9092 (for TCP connections) are exposed.
- **Start Command**: The container launches the H2 server with flags enabling both web and TCP access, and ensures the database is initialized if it doesn’t already exist.


## Web Dockerfile

Following the setup of the database service, I created a `Dockerfile` for the `web` application, placing it within the web directory. This file is responsible for preparing the container that builds and serves both the backend and frontend components of the application.
The configuration is as follows:

```dockerfile
FROM tomcat:10.1-jdk21

# Install essential tools
RUN apt-get update && apt-get install -y git curl npm && rm -rf /var/lib/apt/lists/*

# Set up SSH for private repo access
WORKDIR /root/.ssh
COPY id_rsa /root/.ssh/id_rsa
RUN chmod 600 id_rsa && ssh-keyscan -H github.com >> known_hosts

# Clone the application source code
WORKDIR /usr/src/app
RUN git clone git@github.com:GasparISEP/devops-24-25-1241907.git .

# Move to the project directory containing React and Spring Boot code
WORKDIR /usr/src/app/CA2/part1/part3/react-and-spring-data-rest-basic

# Build the React frontend
RUN npm install
RUN npm run build  # ou npm run webpack, se for o que deseja invocar

# Compile the backend and package it as a WAR file
ENV GRADLE_USER_HOME=/usr/src/app/.gradle
RUN chmod +x gradlew
RUN ./gradlew clean build
RUN ./gradlew bootWar

# Deploy the WAR file to Tomcat
RUN cp build/libs/basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/basic-0.0.1-SNAPSHOT.war

EXPOSE 8080
```

##### Explanation:

- **Base Image**: This container is built on top of tomcat:10.1-jdk21, which includes a lightweight Tomcat server bundled with OpenJDK 21.
- **System Preparation**: Tools like Git, curl, and npm are installed to allow project setup and frontend build execution.
- **SSH Configuration**: SSH credentials are configured to allow secure access to the private GitHub repository.
- **Source Code Cloning**: The repository is cloned into the container’s working directory.
- **Frontend Build**: The React application is installed and built, producing static files for deployment.
- **Backend Compilation**: Using Gradle, the backend is compiled and packaged into a WAR archive.
- **WAR Deployment**: The compiled WAR file is placed in Tomcat’s deployment directory so the application can be served.
- **Port Binding**: Port 8080 is opened to allow incoming web requests to reach the application.

## Docker Compose

To orchestrate the database and web application containers, I created a docker-compose.yml file that defines how both services are built, connected, and executed.

```yaml
services:
  web:
    build: ./web
    ports:
      - "8080:8080"
    networks:
      my_custom_network:
        ipv4_address: 192.168.56.10
    depends_on:
      - db

  db:
    build: ./db
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - ./data:/usr/src/data-backup
    networks:
      my_custom_network:
        ipv4_address: 192.168.56.11

networks:
  my_custom_network:
    driver: bridge
    ipam:
      config:
        - subnet: "192.168.56.0/24"

volumes:
  db_data: {}
```

##### Explanation:

- **Compose Version**: Uses version 3.7, which supports advanced networking features and service dependencies.
- **SServices Configuration**:
    - **Web Service**:
        - Built from the ./web directory.
        - Exposes port 8080 to the host machine.
        - Assigned a static IP (192.168.56.10) within the my_custom_network.
        - Declares a dependency on the db service to ensure it starts first.

    - **Database Service**:
        - Built from the ./db directory.
        - Exposes ports 8082 (web interface) and 9092 (TCP connection).
        - Mounts a local volume (./data) into the container for backup purposes.
        - Assigned a static IP (192.168.56.11) within the same custom network.

- **Network Definition**:
    - my_custom_network is a custom bridge network with manual IP assignment, defined directly in the Compose file using the ipam configuration.
    - This setup allows for precise container communication through fixed IP addresses, which can simplify testing and integration.

To build and launch the services specified in the `docker-compose.yml` file, I used the following command:

```bash
docker-compose up --build
```

Once the services were up and running, the web application was accessible at http://localhost:8080/basic-0.0.1-SNAPSHOT/, while the H2 database console could be reached at http://localhost:8082, and connected to it using the JDBC URL jdbc:h2:tcp://192.168.56.11:9092/./jpadb.

<img src="https://i.imgur.com/9dFUJ9k.png" width="800"/>

<img src="https://i.imgur.com/U6Ctte0.png" width="800"/>

## Tag and Push Images

To verify that the images were properly tagged and uploaded to my Docker Hub repository, I carried out the following steps:

I started by listing all available Docker images and their corresponding IDs using the command below:

```bash
docker images
```

These commands labeled the `part4-web` image with the `latest` tag and the `part4-db` image also with the `latest tag, as reflected in the output of the docker images command.

<img src="https://i.imgur.com/kxjojNE.png" width="800"/>

This step helped me identify the Image IDs of the specific images I intended to tag and upload to Docker Hub.

Next, I assigned appropriate tags to the images, including the target repository name and version tag. Using the Image IDs retrieved earlier, I ran the following commands:

```bash
docker tag 067fbb196338 gasparfigueiras/part4-web:latest
docker tag 7857ea26ad31 gasparfigueiras/part4-db:latest
```

Prior to pushing the images, I authenticated with my Docker Hub account using the following command:

```bash
docker login
```

Finally, I uploaded the images to my Docker Hub repository by executing the following commands:

```bash
docker push gasparfigueiras/part4-web:latest
docker push gasparfigueiras/part4-db:latest
```

<img src="https://i.imgur.com/a6NIa5z.png" alt="Docker images" width="800">


## Working with volumes

To verify that the database file was correctly stored in the mounted volume, I used the docker-compose exec command to access the running db container and manually copy the required file:

```bash
docker-compose exec db bash
```

Within the container’s shell, I manually copied the h2-1.4.200.jar file into the mounted volume directory:

```bash
cp /usr/src/app/h2-1.4.200.jar /usr/src/data-backup
exit
```

This sequence of commands accesses the db container, transfers the specified file to the mounted volume directory, and then exits the container.
This process guarantees that the database file is safely backed up in the volume and remains persistent on the host system.

## Conclusion

Through this project, I achieved the successful containerization of both a web application and its database using Docker, coordinating their deployment with Docker Compose.
The workflow included crafting individual Dockerfiles for each service, configuring volumes to ensure data persistence, and managing the services via Docker Compose.