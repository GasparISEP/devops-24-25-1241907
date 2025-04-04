# CA1: Part 2 - Build Tools with Gradle

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

## Table of Contents

- [Introduction](#introduction)

- [Environment Setup](#environment-setup)

- [Gradle Basic Demo](#gradle-basic-demo)

- [Add a new task](#add-a-new-task)

- [Add a unit test](#add-a-unit-test)

- [Add a new task of type Copy](#add-a-new-task-of-type-copy)

- [Add a new task of type Zip](#add-a-new-task-of-type-zip)

- [Conclusion](#conclusion)

## Introduction

This report presents the work carried out for the **Build Tools with Gradle** assignment in the DevOps course, with a focus on the practical application of Gradle.
The task is designed to provide hands-on experience with this build automation tool, covering everything from initial setup to advanced features such as task creation and unit testing.

Following the **Environment Setup** phase, the report describes the implementation of the **Gradle Basic Demo**, a multithreaded chat server.
This example illustrates the process of building, running, and connecting multiple clients to the server, showcasing Gradle’s capabilities in managing real-world applications.

Next, the **Add a new task** section explains how a custom Gradle task was introduced to the project, demonstrating the flexibility of Gradle in extending functionality according to project requirements.

The **Add a unit test** section shifts the focus to improving software reliability by integrating tests into the build process.
The subsequent sections, **Add a new task of type Copy** and **Add a new task of type Zip**, highlight how Gradle can be used for file manipulation, which is essential for project maintenance and distribution.

Finally, the Conclusion section reflects on the skills acquired throughout the assignment, the challenges encountered, and the practical knowledge gained in using Gradle for software development tasks.

## Environment Setup

The first step involved creating a new directory for the assignment, **/CA1/Part2**, and then cloning the example application from the provided Bitbucket repository.
This repository included a `build.gradle` file and the Gradle Wrapper, which ensured a consistent build environment.
After completing the installation, I verified the Gradle setup by running `./gradlew -v` in the command line.

Next, I integrated the project into my Integrated Development Environment (IDE) that supports Gradle, allowing me to take advantage of its features.
To confirm that the project's configuration was correct and ready for development, I executed a basic Gradle build.
This validation step was crucial to ensure that all components were properly set up.

These preliminary steps established a solid foundation, enabling me to seamlessly proceed with the subsequent tasks outlined in the assignment.

## Gradle Basic Demo

The **Gradle Basic Demo** acted as a practical exercise focused on a multithreaded chat server, showcasing its capability to manage multiple clients at the same time.

**Build Process:**

<img  src="https://i.imgur.com/ZEAjWZY.png"  width="1000">

**Server Startup:**

Next, I started the chat server by running the command `java -cp build/libs/basic_demo-0.1.0.jar basic_demo.ChatServerApp 59001`.

<img  src="https://i.imgur.com/7Myx1LD.png"  width="1000">

**Client Connections:**

For the client side, I established connections to the chat server by executing `./gradlew runClient`, ensuring each client connected to localhost on port 59001.
The `build.gradle` file was configured to allow easy modifications for different connection settings.
To showcase the server’s ability to handle multiple clients, I launched several client instances from different terminal windows.

<img src="https://i.imgur.com/3lwpm4i.png" width=450>

<img src="https://i.imgur.com/SXymq7q.png" width=450>

<img src="https://i.imgur.com/b8kNLHW.png" width=450>

## Add a new task

I added a `runServer` task to the `build.gradle` file to improve our development workflow by simplifying the server startup process.
This new task allows us to launch the chat server directly through a Gradle command, removing the need for manual command-line inputs each time we start the server.

The `runServer` task is defined in the `build.gradle` file as follows: it is set as a JavaExec task to execute Java applications, depends on the classes task to ensure all necessary classes are compiled before starting the server, and is configured to launch the ChatServerApp main class on port 59001.

```java
task runServer(type:JavaExec, dependsOn: classes){
    group = "DevOps"
    description = "Launches a chat server that listens on port 59001"

    classpath = sourceSets.main.runtimeClasspath

    mainClass = 'basic_demo.ChatServerApp'

    args '59001'
}
```

To verify this addition’s functionality, I executed the task using `./gradlew runServer` in the command line.
The immediate feedback from the terminal confirmed the task’s successful execution, demonstrating the server’s active status.
This enhancement has significantly streamlined our development process, making it more efficient by reducing the steps required to start the server.

<img  src="https://i.imgur.com/qgxHXiT.png"  width="700">

This integration into our Gradle build script not only showcases Gradle’s flexibility as a build tool but also significantly enhances our project’s overall productivity by automating routine tasks.

## Add a unit test

I added a unit test to verify the functionality of the App class.
The test was placed in a new directory, `src/test/java/basic_demo`, within a file named `AppTest.java`.
This test checks whether the App class provides a non-null greeting message, a basic yet essential feature.

To ensure the test environment was properly configured, I added the JUnit dependency to the `build.gradle` file, which is crucial for running unit tests.

```java
testImplementation 'junit:junit:4.12'
```

This inclusion ensures that the project correctly recognizes and executes JUnit tests without issues.

```java
package basic_demo;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void testAppHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
```

To run the test, I executed the command `./gradlew test`.

<img  src="https://i.imgur.com/Fp8WqPt.png"  width="1000">

## Add a new task of type Copy

The next step was to introduce a Copy task in the `build.gradle` file to create a backup of the source code.
This ensures that a stable version is always available in case of unexpected issues during development.

The backup task utilizes Gradle’s Copy task type, as outlined below, to duplicate the contents of the src directory into a specified backup location within the project.
This approach helps maintain an up-to-date version of the codebase, particularly before making major modifications or updates.

```java
task backup(type: Copy) {
    group = "DevOps"
    description = "Copies the sources of the application to a backup"

    from 'src'
    into 'backup'
}
```

To verify the task’s functionality, I executed `./gradlew backup` from the command line.
The task completed successfully, as shown in the terminal output.
This confirms that the source code was copied to the designated backup location, proving the task’s reliability in preserving the project’s code.

<img  src="https://i.imgur.com/UtOpEfb.png"  width="1000">

While the backup folder is not included in the remote repository, it has been successfully created in my local development environment.
This is confirmed by its presence in my local file system after executing the backup task in Gradle.

<img  src="https://i.imgur.com/vQYKWKI.png"  width="300">

Integrating the backup task into the Gradle build script has enhanced the project’s resilience by enabling straightforward and reliable code backups.

## Add a new task of type Zip

The last step was to define a Zip task to compress the project’s source code into a .zip file.

This task streamlines the process of packaging the src directory into a compressed file, making it useful for backups or distribution.
It plays a key role in archiving different versions of the project and preparing the code for sharing or deployment.

```java
task archive(type: Zip) {
    group = "DevOps"
    description = "Creates a zip archive of the source code"

    from 'src'
    archiveFileName.set('src_backup.zip')
    destinationDirectory.set(file('build'))
}
```

After defining the zip task, I executed it using the command `./gradlew archive`.
The terminal output confirmed its successful execution, indicating that the src directory was successfully compressed into a .zip archive.

<img  src="https://i.imgur.com/xDmS6eY.png"  width="1000">

While the ZIP file is not included in the remote repository, it has been successfully generated in my local development environment.
This is confirmed by its presence in my local file system after executing the archive task in Gradle.

<img  src="https://i.imgur.com/vQYKWKI.png"  width="300">

## Conclusion

Completing this assignment provided valuable insights into the practical use of Gradle as a build tool.
The tasks carried out emphasized Gradle’s flexibility and versatility in managing various aspects of the development process.

Automating build processes, integrating unit tests, and handling file operations demonstrated Gradle’s capabilities in maintaining a streamlined and efficient workflow.
The addition of new tasks to the `build.gradle` file showcased Gradle’s extensibility.
Tasks like `runServer`, `backup`, and `archive` not only simplified the development process but also enhanced the project’s resilience and distribution options.

Integrating unit tests into the build process reinforced the importance of testing in software development and highlighted how Gradle facilitates this practice.

Overall, this assignment deepened my understanding of Gradle and its role in software development.
The knowledge and skills gained will be valuable in future projects, contributing to more efficient and reliable development workflows.