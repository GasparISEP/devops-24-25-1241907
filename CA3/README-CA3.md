# CA3: CI/CD Pipelines with Jenkins

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

## Table of Contents

- [Introduction](#introduction)
- [Setup and prerequisites](#setup-and-prerequisites)
- [Part 1: Setting up Jenkins Pipeline for Gradle Basic Demo Application](#part-1-setting-up-jenkins-pipeline-for-gradle-basic-demo-application)
- [Part 2: Setting up Jenkins Pipeline for React and Spring Data REST Basic Application](#part-2-setting-up-jenkins-pipeline-for-react-and-spring-data-rest-basic-application)
- [Conclusion](#conclusion)


# Introduction

In this assignment, the goal was to establish CI/CD pipelines using Jenkins for two separate applications developed in earlier projects.
The first application was the Gradle Basic Demo from CA1/Part2, while the second was the React and Spring Data REST Basic Application from CA1/Part3.

This hands-on task allowed me to explore and apply essential CI/CD concepts, using Jenkins’ functionalities to streamline the processes of building, testing, and deploying these applications.
This document provides a thorough walkthrough of each step taken to configure and implement the pipelines.

## Setup and prerequisites

To properly configure Jenkins and establish the CI/CD pipeline, it was crucial to have the following tools and software installed:

- **Git**: served as the version control system and was used to manage repositories. I confirmed its installation by running the `git --version` command.

- **Docker**: was needed to create and manage Docker containers.
I validated the installation with the docker `docker --version`.

- **Jenkins**: functioned as the core tool for configuring the CI/CD pipeline.
I installed Jenkins locally and confirmed it was operational by accessing the Jenkins dashboard through a web browser.

## Part 1: Setting up Jenkins Pipeline for Gradle Basic Demo Application

The initial phase of the CA3 assignment focused on configuring a Jenkins pipeline for the Gradle Basic Demo application from CA1/Part2.
The pipeline itself was outlined within a Jenkinsfile located in the project directory. Below is the content of that Jenkinsfile:

```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out from repository'
                git url: 'https://github.com/GasparISEP/devops-24-25-1241907.git', branch: 'main'
            }
        }
        stage('Assemble') {
            steps {
                dir('CA2/part1/part2/gradle_basic_demo') {
                    echo 'Assembling...'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean assemble'
                }
            }
        }
        stage('Test') {
            steps {
                dir('CA2/part1/part2/gradle_basic_demo') {
                    echo 'Running Tests...'
                    sh './gradlew test'
                    junit '**/build/test-results/**/*.xml'
                }
            }
        }
        stage('Archive') {
            steps {
                dir('CA2/part1/part2/gradle_basic_demo') {
                    echo 'Archiving artifacts...'
                    archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
                }
            }
        }
    }
}
```

The pipeline consisted of four stages:

- **Checkout**: This phase retrieved the project code from the GitHub repository.

- **Assemble**: This phase compiled the Gradle project by running `./gradlew clean assemble`.

- **Test**: This phase executed the unit tests for the project using the `./gradlew test` command.

- **Archive**: This phase stored the resulting JAR file as a build artifact.

To set up a new pipeline job in Jenkins, I accessed the Jenkins dashboard and selected `New Item`. I provided a name for the job and chose the `Pipeline` option. Within the pipeline configuration, I selected `Pipeline script from SCM`, specified `Git` as the SCM, entered the repository URL, and set `main` as the branch.
The Script Path was configured to CA3/JenkinsFile-gradle-basic-demo. Once these settings were applied, I saved the configuration.

To execute the pipeline, I clicked `Build Now` and observed the build process in the Jenkins console output. The following image illustrates the Jenkins pipeline job following a successful build:

<img src="https://i.imgur.com/6yhWyfJ.png">

## Part 2: Setting up Jenkins Pipeline for React and Spring Data REST Basic Application

The second component of the CA3 assignment required configuring a Jenkins pipeline for the React and Spring Data REST Basic Application originally developed in CA1/Part3.
The pipeline configuration was specified in a Jenkinsfile placed within the project’s directory. Below is the content of that file:

```groovy
pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-creds-id'
        DOCKER_IMAGE          = 'gasparfigueiras/springboot-app'
        DOCKER_REGISTRY       = 'https://index.docker.io/v1/'
        REPO_URL              = 'https://github.com/GasparISEP/devops-24-25-1241907.git'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out...'
                git branch: 'main', url: env.REPO_URL
            }
        }

        stage('Create Dockerfile') {
            steps {
                dir('CA2/part1/part3/react-and-spring-data-rest-basic') {
                    echo 'Writing Dockerfile...'
                    script {
                        writeFile file: 'Dockerfile', text: '''
   FROM eclipse-temurin:21-jdk
   WORKDIR /app
   COPY build/libs/*.war app.war
   EXPOSE 8080
   ENTRYPOINT ["java","-jar","app.jar"]
   '''

                    }
                }
            }
        }

        stage('Assemble') {
            steps {
                dir('CA2/part1/part3/react-and-spring-data-rest-basic') {
                    echo 'Assembling...'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean assemble'
                }
            }
        }

        stage('Test') {
            steps {
                dir('CA2/part1/part3/react-and-spring-data-rest-basic') {
                    echo 'Testing...'
                    sh './gradlew test'
                    junit '**/build/test-results/**/*.xml'
                }
            }
        }

        stage('Javadoc') {
            steps {
                dir('CA2/part1/part3/react-and-spring-data-rest-basic') {
                    echo 'Generating Javadoc…'
                    sh './gradlew javadoc'
                    publishHTML(target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir:   'build/docs/javadoc',
                            reportFiles: 'index.html',
                            reportName:  'Javadoc'
                    ])
                }
            }
        }

        stage('Archive') {
            steps {
                dir('CA2/part1/part3/react-and-spring-data-rest-basic') {
                    echo 'Archiving...'
                    archiveArtifacts artifacts: 'build/libs/*.war', fingerprint: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('CA2/part1/part3/react-and-spring-data-rest-basic') {
                    echo 'Building & Pushing Docker image...'
                    script {
                        def app = docker.build("${env.DOCKER_IMAGE}:${env.BUILD_NUMBER}", '.')
                        docker.withRegistry(env.DOCKER_REGISTRY, env.DOCKER_CREDENTIALS_ID) {
                            app.push()
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
```

This Jenkins pipeline was structured into seven distinct stages:

- **Checkout**: The codebase was retrieved from the GitHub repository.

- **Create Dockerfile**: A Dockerfile was dynamically generated for the Spring Boot application.

- **Assemble**: The project was compiled using the Gradle `./gradlew clean assemble` command.

- **Test**: Unit tests were executed by running `./gradlew test`.

- **Javadoc**: Documentation was generated using Javadoc.

- **Archive**: The resulting WAR file was stored as a Jenkins artifact.

- **Build Docker Image**: A Docker image for the Spring Boot application was built and pushed to Docker Hub.

In order for Jenkins to execute Docker commands within the pipeline, a custom Jenkins container was created.

```dockerfile
FROM jenkins/jenkins:lts

USER root
RUN apt-get update \
 && apt-get install -y docker.io \
 && rm -rf /var/lib/apt/lists/*

USER jenkins
```

By mounting the host’s Docker socket into the container (`-v /var/run/docker.sock:/var/run/docker.sock`), the pipeline gains the ability to build and push Docker images directly from within Jenkins.

To configure Docker Hub credentials in Jenkins, I accessed `Manage Jenkins` > `Manage Credentials` > `System` > `Global credentials` (unrestricted) > `Add Credentials`.
I selected `Username with password`, entered my Docker Hub credentials, and assigned the ID dockerhub-creds-id.

To set up the pipeline job, I clicked `New Item` in Jenkins, provided a job name, and selected the `Pipeline` option.
Within the configuration panel, I chose `Pipeline script from SCM`, selected `Git` as the source control system, inserted the repository URL, set the branch to `main`, and specified the script path as CA3/JenkinsFile-react-and-spring-data-rest-basic. I then saved the setup.

To execute the pipeline, I clicked `Build Now` and monitored the build progress via the Jenkins console output.
The image below displays the Jenkins job after a successful execution:

<img src="https://i.imgur.com/ko3FoQb.png">

This configuration provides full automation of the build, testing, documentation, and containerization processes for the React and Spring-based application.

## Conclusion

In summary, this assignment offered practical experience in configuring CI/CD pipelines with Jenkins for two distinct applications.
Through the setup of pipelines for both the Gradle Basic Demo and the React with Spring Data REST applications, I enhanced my understanding of continuous integration and deployment workflows.
The process involved defining pipeline stages, running build and test commands, generating documentation, archiving artifacts, and creating Docker images.
Automating these steps not only reduces the need for manual actions but also helps prevent errors and speeds up the overall software delivery process.