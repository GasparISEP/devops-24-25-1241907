# CA1: Part 3 - Build Tools with Gradle

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

### Table of Contents

- [Introduction](#introduction)

- [Set Up Initial Gradle Project](#set-up-initial-gradle-project)

- [Integrate Existing Code](#integrate-existing-code)

- [Configure Frontend Plugin for Gradle](#configure-frontend-plugin-for-gradle)

- [Add Gradle Tasks for File Management](#add-gradle-tasks-for-file-management)

- [Alternative Solution](#alternative-solution)

- [Conclusion](#conclusion)


## Introduction

This document provides a thorough overview of the activities completed in the third segment of Class Assignment 1 for the DevOps course, emphasizing the application of Gradle as a build automation tool.  
The assignment comprises a well-structured series of tasks aimed at transitioning a Spring Boot application from Maven to Gradle, illustrating the practical advantages and uses of Gradle in the software development lifecycle.  

The **Set Up Initial Gradle Project** section outlines the essential project setup.  
**Integrate Existing Code** elaborates on the adaptation of the application’s source code into the Gradle project framework.  
**Configure Frontend Plugin for Gradle** discusses the incorporation of a plugin for managing frontend assets.  
In **Add Gradle Tasks for File Management**, tailored Gradle tasks are created to enhance project maintenance.  
An **Alternative Solution** investigates other build tools, while the **Conclusion** reflects on the learning journey and the significance of Gradle in contemporary software development practices.

## Set Up Initial Gradle Project

The initial configuration of the Gradle project consisted of several crucial steps required for the transition from a Maven-based structure to a Gradle-based one.  
The process commenced with the creation of a dedicated branch for this section of the assignment, ensuring that the project's setup and subsequent modifications remained isolated and manageable.

```bash
git branch tut-basic-gradle
git switch tut-basic-gradle
```

Next, a new Spring Boot project was initialized using the Spring Initializr web interface at [Spring Initializr](https://start.spring.io/).  
The project was set up to include essential dependencies such as Rest Repositories, Thymeleaf, JPA, and H2, ensuring that all necessary modules were available for the application's functionality and managed by Gradle.

The generated `.zip` file, containing the project skeleton, was downloaded and extracted into the `CA1/Part3/` folder within the repository.  
This structure established the foundation for an empty Spring application, ready to be built using Gradle.  
To confirm the project setup and check the available Gradle tasks, the following command was executed in the project's root directory.

```bash
./gradlew tasks
```

The output of this command displayed a comprehensive list of available tasks and functionalities that can be executed using the Gradle build tool.

```bash
> Task :tasks

------------------------------------------------------------
Tasks runnable from root project 'react-and-spring-data-rest-basic'
------------------------------------------------------------

Application tasks
-----------------
bootRun - Runs this project as a Spring Boot application.
bootTestRun - Runs this project as a Spring Boot application using the test runtime classpath.

Build tasks
-----------
assemble - Assembles the outputs of this project.
bootBuildImage - Builds an OCI image of the application using the output of the bootJar task
bootJar - Assembles an executable jar archive containing the main classes and their dependencies.
build - Assembles and tests this project.
buildDependents - Assembles and tests this project and all projects that depend on it.
buildNeeded - Assembles and tests this project and all projects it depends on.
classes - Assembles main classes.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the classes of the 'main' feature.
resolveMainClassName - Resolves the name of the application's main class.
resolveTestMainClassName - Resolves the name of the application's test main class.
testClasses - Assembles test classes.

Build Setup tasks
-----------------
init - Initializes a new Gradle build.
wrapper - Generates Gradle wrapper files.

Documentation tasks
-------------------
javadoc - Generates Javadoc API documentation for the 'main' feature.

Frontend tasks
--------------
assembleFrontend - Assembles frontend artifacts by running a specific script.
checkFrontend - Checks frontend by running a specific script.
installCorepack - Installs a specific version of Corepack, overriding the one provided by default in Node.js distribution.
installFrontend - Installs frontend dependencies.
installNode - Downloads and installs a Node.js distribution.
installPackageManager - Installs the package manager.
publishFrontend - Publishes frontend artifacts by running a specific script.
resolvePackageManager - Resolves the package manager.
runCorepack - Runs a command with corepack executable.
runNode - Runs a command with node executable.
runNpm - Runs a command with npm executable.
runPnpm - Runs a command with pnpm executable.
runYarn - Runs a command with yarn executable.

Help tasks
----------
buildEnvironment - Displays all buildscript dependencies declared in root project 'react-and-spring-data-rest-basic'.
dependencies - Displays all dependencies declared in root project 'react-and-spring-data-rest-basic'.
dependencyInsight - Displays the insight into a specific dependency in root project 'react-and-spring-data-rest-basic'.
dependencyManagement - Displays the dependency management declared in root project 'react-and-spring-data-rest-basic'.
help - Displays a help message.
javaToolchains - Displays the detected java toolchains.
outgoingVariants - Displays the outgoing variants of root project 'react-and-spring-data-rest-basic'.
projects - Displays the sub-projects of root project 'react-and-spring-data-rest-basic'.
properties - Displays the properties of root project 'react-and-spring-data-rest-basic'.
resolvableConfigurations - Displays the configurations that can be resolved in root project 'react-and-spring-data-rest-basic'.
tasks - Displays the tasks runnable from root project 'react-and-spring-data-rest-basic'.

Publishing tasks
----------------
publish - Publishes all publications produced by this project.

Verification tasks
------------------
check - Runs all checks.
test - Runs the test suite.

Rules
-----
Pattern: clean<TaskName>: Cleans the output files of a task.
Pattern: build<ConfigurationName>: Assembles the artifacts of a configuration.

To see all tasks and more detail, run gradlew tasks --all

To see more detail about a task, run gradlew help --task <task>

BUILD SUCCESSFUL in 719ms
1 actionable task: 1 executed
```

## Integrate Existing Code

This phase of the project focused on integrating the existing codebase from a basic tutorial setup into the newly structured Gradle project.  
The process was carefully executed to ensure that all components functioned correctly within the new build management system.  

The following steps were undertaken to incorporate the existing code into the Gradle project.

- **Replace the Source Directory** 

The original `src` directory in the Gradle project was removed to allow for the integration of the existing codebae.
The `src` folder, along with its subdirectories, was copied from the basic tutorial project into the new Gradle project structure.  

- **Include Additional Configuration Files**

Key configuration files, including `webpack.config.js` and `package.json`, were transferred to the root directory of the new project to maintain the frontend build settings and dependencies.  

- **Remove Unnecessary Directories**

After migration, the `src/main/resources/static/built` directory was deleted. Since Webpack automatically generates this directory during the build process, it should not be manually included in version control to prevent conflicts and redundancy.

The following steps were implemented to resolve a compilation error that occurred after migrating the codebase from `CA1-Part1` to `CA1-Part3`.

- **Updating Import Statements**

To align with the updated project dependencies and the transition from Java EE to Jakarta EE, modifications were made to Java classes.  
In the `Employee.java` class, import statements were adjusted from `javax.persistence` to `jakarta.persistence`.

- **Configuring the Package Manager**

The `package.json` file was updated to specify a fixed version of the package manager by adding `"packageManager": "npm@9.6.7"`.
This ensures consistency in the package manager version across different environments.

Following the successful integration and configuration adjustments, the application was tested to validate its operational stability.

- **Starting the Application**

The command `./gradlew bootRun` was executed to compile and launch the backend.  

- **Checking the Frontend**:

Navigating to http://localhost:8080 in a web browser displayed an empty page.  
This is expected at this stage, as the Gradle setup currently lacks a necessary plugin to manage frontend code.  
This limitation will be addressed in later stages of the project.

This approach ensures that the foundational codebase is effectively integrated into the Gradle environment, laying the groundwork for further enhancements and the implementation of more advanced functionalities.


To integrate frontend build processes with the newly adopted Gradle system, the `org.siouan.frontend-gradle-plugin` was introduced.
This plugin plays a vital role in managing frontend assets, similar to how the `frontend-maven-plugin` functions in Maven-based projects.


- **Adding the Plugin**

The Gradle build script was modified to include the appropriate `org.siouan.frontend` plugin corresponding to the Java version used in the project. For Java 17, the following line was added to the `plugins` block in `build.gradle`:
  
```groovy
id 'org.siouan.frontend-jdk21' version '10.0.0'
```

- **Configuring the Plugin**

To properly manage frontend assets, configurations specifying the Node.js version and relevant script commands were added to `build.gradle`.
This setup ensures that the correct version of Node.js is utilized and defines scripts for assembling, and verifying the frontend.

```groovy
frontend {
    nodeVersion = "18.17.1"
    assembleScript = "run build"
    checkScript = "run check"
}
```

- **Updating package.json**

The `scripts` section in `package.json` was modified to handle the execution of Webpack and other frontend-related tasks efficiently.

```json
"scripts": {
"watch": "webpack --watch -d --output ./target/classes/static/built/bundle.js",
"webpack": "webpack",
"build": "npm run webpack",
"check": "echo Checking frontend",
"clean": "echo Cleaning frontend",
"lint": "echo Linting frontend",
"test": "echo Testing frontend"
}   }
```

- **Validating the Configuration**

After setting up the frontend plugin, both the build process and runtime functionality were tested.

- **Build Verification**:
Running `./gradlew build` confirmed that the project successfully compiled with the frontend integration in place.

- **Application Execution**:
The command `./gradlew bootRun` was executed, and the application was accessed at http://localhost:8080.
Unlike previous stages, the webpage now displayed frontend content, demonstrating that the Gradle plugin correctly managed frontend resources during both the build and runtime processes.

This configuration highlights the seamless integration of frontend build management within the Gradle environment, strengthening the project's ability to support complex full-stack development workflows efficiently.

## Add Gradle Tasks for File Management

To improve file management within the project, particularly in handling distribution and cleanup processes, two custom Gradle tasks were introduced: `copyJar` and `cleanWebpack`.

### 1. **Task: `copyJar`**
- **Purpose**

This task copies the `.jar` file generated by the `bootJar` task from the output directory to a `dist` folder at the project root.
This ensures that only the correctly built `.jar` file is included for distribution, reducing errors and guaranteeing that deployments use the most up-to-date build.

- **Configuration**
```groovy
task copyJar(type: Copy) {
    dependsOn bootJar
    from bootJar.outputs
    into file("dist")
}
```

- **Dependencies**

The task explicitly depends on `bootJar`, ensuring that the `.jar` file is copied only after the `bootJar` task completes, maintaining a structured and reliable build process.

### 2. **Task: `cleanWebpack`**

- **Purpose**

This task removes all files generated by Webpack, located in the `src/main/resources/static/built` directory.
It keeps the build environment clean, ensuring that each build includes only the necessary files and prevents conflicts from outdated or stale files.

- **Configuration**

```groovy
task cleanWebpack(type: Delete) {
    delete 'src/main/resources/static/built'
}
clean.dependsOn cleanWebpack
```
- **Dependencies**

It is configured to run before Gradle’s standard `clean` task, integrating it into the existing cleanup workflow.

### **Task Execution and Validation**

Each task was tested to confirm its correct functionality

- **Executing `copyJar`**
    - **Command**: `./gradlew copyJar`
    - **Result**: The `.jar` file produced by `bootJar` was successfully copied to the `dist` directory. This verified that the task correctly identifies and relocates the expected artifact, ensuring it is ready for distribution.


- **Executing `cleanWebpack`**
    - **Command**: `./gradlew cleanWebpack`
    - **Result**: The contents of `src/main/resources/static/built` were successfully deleted. This confirmed that the cleanup task operates as intended, helping maintain an orderly build environment.

These custom tasks were incorporated into the Gradle build process to automate file management, improving efficiency and reliability in the project's build and deployment workflows.
Their successful execution confirms their contribution to streamlined project maintenance.

## Alternative Solution

**Implementing the Assignment Goals with Apache Ant and Ivy**

To replicate the setup and functionality achieved with Gradle, I will outline the steps required to configure Apache Ant as the build tool for a Spring Boot application.
This alternative mirrors the Gradle setup, including frontend asset integration, custom build targets, and file handling.
Here is a step-by-step guide on setting up Apache Ant with Ivy to manage the build lifecycle and dependency resolution.

- **Project Setup:**

The build process is orchestrated via a build.xml file.
Dependencies required by the Spring Boot backend—such as JPA, Thymeleaf, and H2—are declared within an ivy.xml descriptor.
Ivy is responsible for resolving and retrieving these libraries into a local lib directory.

```xml
<ivy-module version="2.0">
  <info organisation="com.example" module="spring-boot-ant-app"/>
  <dependencies>
    <dependency org="org.springframework.boot" name="spring-boot-starter-data-jpa" rev="3.2.3"/>
    <dependency org="org.springframework.boot" name="spring-boot-starter-thymeleaf" rev="3.2.3"/>
    <dependency org="org.springframework.boot" name="spring-boot-starter-data-rest" rev="3.2.3"/>
    <dependency org="com.h2database" name="h2" rev="2.2.224"/>
  </dependencies>
</ivy-module>
```

Additionally, an ivysettings.xml file was configured to retrieve dependencies from Maven Central.

```xml
<ivysettings>
  <settings defaultResolver="central"/>
  <resolvers>
    <ibiblio name="central" m2compatible="true"/>
  </resolvers>
</ivysettings>
```

- **Frontend Integration:**


To handle Node.js and npm processes, Ant’s exec task is used to install frontend dependencies and trigger the build.
This approach assumes a React application inside src/main/resources/static.

```xml
<target name="npm-build">
  <exec executable="npm" dir="src/main/resources/static">
    <arg value="install"/>
  </exec>
  <exec executable="npm" dir="src/main/resources/static">
    <arg value="run"/>
    <arg value="build"/>
  </exec>
</target>
```

- **Copy JAR Task:**

To move the final JAR file to a dist/ folder, Ant’s copy task was used in a custom target.

```xml
<target name="copy-jar">
  <copy file="build/app.jar" tofile="dist/app.jar"/>
</target>
```

- **Delete Webpack Files Task:**

To delete frontend-generated assets (e.g., from a previous Webpack build), the delete task is configured as part of the clean phase.

```xml
<target name="clean">
  <delete dir="build"/>
  <delete dir="dist"/>
  <delete>
    <fileset dir="src/main/resources/static/build">
      <include name="**/*"/>
    </fileset>
  </delete>
</target>
```

This prevents stale frontend files from persisting between builds.


**Comparison Between Ant and Gradle**

This table provides a concise overview of the core differences between Ant+Ivy and Gradle.

| Feature                   | Ant + Ivy                                            | Gradle                                                                  |
|---------------------------|------------------------------------------------------|-------------------------------------------------------------------------|
| **Build Language**        | XML-based procedural configuration.                  | Uses Groovy or Kotlin DSL for configuration scripts.                    |
| **Performance**           | Depends on manual task setup.                        | Faster, supports incremental builds and up-to-date checks.              |
| **Flexibility**           | Very flexible, but verbose and manual.               | Highly customizable and flexible due to scripting support.              |
| **Dependency Management** | Requires Ivy, more setup needed.                     | Offers powerful dependency management with dynamic versions.            |
| **Ease of Use**           | Less intuitive, requires explicit wiring of tasks.   | Steeper learning curve but more powerful due to flexibility.            |
| **Plugins**               | Fewer available plugins; relies on task composition. | Extensive plugin ecosystem; easier to write and apply custom plugins.   |
| **Community and Support** | Declining usage, fewer modern examples.              | Growing community, well-supported with ample documentation.             |
| **Use Case**              | Suited for fine-grained control and legacy projects. | Ideal for multi-project builds and projects needing high customization. |

Ant combined with Ivy provides a viable alternative to Gradle when precise control over the build lifecycle is desired.
Though it requires more explicit configuration and lacks the higher-level abstractions provided by Gradle, it remains fully capable of managing Spring Boot applications with integrated frontend builds, JAR packaging, and file management.
This setup demonstrates Ant’s adaptability, allowing complex build flows to be modeled through custom tasks and sequencing logic.

## Conclusion

This technical report presents the migration of a Spring Boot application from Maven to Gradle, highlighting Gradle’s advanced capabilities in dependency management, frontend integration, and task customization.

This migration process has deepened my practical knowledge of build automation and emphasized the importance of selecting the right tool based on the specific needs of a project and the team’s skill set.
The insights gained will inform future decisions, contributing to more streamlined and effective software development processes.