# CA2: Part 1 - Virtualization with Vagrant

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

### Table of Contents

## Table of Contents

- [Introduction](#introduction)

- [Create a VM](#create-a-vm)

- [Configure Network and Services](#configure-network-and-services)

- [Clone the Repository](#clone-the-repository)

- [Set Up Development Environment](#set-up-development-environment)

- [Execute the spring boot tutorial basic project](#execute-the-spring-boot-tutorial-basic-project)

- [Execute the gradle_basic_demo project - Part 1](#execute-the-gradle_basic_demo-project---part-1)

- [Execute the gradle_basic_demo project - Part 2](#execute-the-gradle_basic_demo-project---part-2)

- [Conclusion](#conclusion)


## Introduction

This technical report presents the procedures and results of **Class Assignment 2 - Part 1**, which focused on virtualization practices using UTM as part of the DevOps curriculum.
The primary aim was to acquire practical experience in deploying and handling virtual environments, a fundamental aspect of modern software engineering and operations.

The report outlines the steps taken to create and configure a virtual machine, establish a suitable development environment, and carry out several assigned projects.

## Creating a Virtual Machine with UTM

- The process began by downloading and installing UTM from https://mac.getutm.app, which provides a user-friendly interface for virtual machine management on macOS.

- I opened UTM and initiated the creation of a new virtual machine with the operating system I planned to install.

- I assigned a name to the VM and selected the appropriate system settings, including the operating system type and version.

- I configured a new virtual hard disk with sufficient storage to support the system requirements.

- In the boot section, I attached the Ubuntu ISO file as the boot medium.
Once the setup was complete, I launched the VM and followed the installation prompts provided by the Ubuntu installer.

- Following the OS installation, I adjusted the VM configuration for improved usability.

- For networking, I configured the default shared network mode to allow internet access.
Additionally, I set up a separate bridged or host-only network interface to allow isolated communication between the host and the virtual machine.


## Configure Network and Services

Once the virtual machine was successfully set up, I moved on to configuring the network and essential services to improve the VM’s functionality and accessibility.

- By default, UTM provides a shared network interface, which allows the VM to access the internet through the host’s connection.
However, to enable isolated communication between the host and the VM, I added an additional network interface in “Bridged” or “Host” mode from the VM configuration settings in UTM.

- I then assigned a static IP address within a private subnet (e.g., `192.168.56.5`), ensuring it was within the appropriate range and did not conflict with other devices.

- I updated the package sources by running `sudo apt update`.

- To assist with manual network management, I installed the networking utilities with `sudo apt install net-tools`.

- I edited the network configuration using Netplan by modifying the file located at /etc/netplan/01-netcfg.yaml.
The configuration was adjusted to define the static IP, gateway, and DNS settings appropriate for the secondary interface.

```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    enp0s3:
      dhcp4: yes
    enp0s8:
      addresses:
        - 192.168.56.5/24
```

- Once the network configuration file was updated, I applied the changes using the `sudo netplan apply` command.

- To enable remote access to the virtual machine, I installed the OpenSSH server by executing `sudo apt install openssh-server`.
After that, I modified the SSH configuration file located at `/etc/ssh/sshd_config`, ensuring that the line `PasswordAuthentication yes` was uncommented to allow password-based login.
I then restarted the SSH service with `sudo service ssh restart`.

- For file transfer purposes, I also set up an FTP server. This was done by installing vsftpd with the command `sudo apt install vsftpd`.
To allow users to upload files, I edited the configuration file `/etc/vsftpd.conf`, uncommented the line `write_enable=YES`, and restarted the FTP service by running `sudo service vsftpd restart`.


## Clone the Repository

- Inside the VM, I created a new SSH key pair to enable encrypted access to GitHub.
This was done via the terminal using:

```bash
ssh-keygen -t ed25519 -C "email@example.com"
```

- Once the key was generated, I retrieved its public part by running:

```bash
cat ~/.ssh/id_ed25519.pub
```

- I then logged into my GitHub account on the host macOS machine, went to Settings → SSH and GPG keys, and clicked New SSH key.
There, I pasted the key content and saved it, allowing my virtual machine to securely authenticate with GitHub over SSH.

- After the key was successfully added, I used the following command within the VM to clone my GitHub repository into the target directory:

```bash
git clone git@github.com:userName/repositoryName.git
```

This setup ensured a secure and password-less connection between the Ubuntu VM running in UTM and my GitHub repositories.


## Set Up Development Environment

After verifying that the virtual machine was fully operational and properly connected to the network, I moved on to installing the essential tools required for the development work.

- I started by refreshing the package lists and upgrading any outdated software already installed on the system. This was done with the following commands:

```bash
sudo apt update
sudo apt upgrade
```

- To manage version control and source code repositories, I installed Git:

```bash
sudo apt install git
```

- Since the projects involved Java, I installed both the Java Development Kit (JDK) and the Java Runtime Environment (JRE):

```bash
sudo apt install openjdk-21-jdk openjdk-21-jre
```

- After the installation, I set Java 21 as the default version using the following commands:

```bash
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

- I then updated the JAVA_HOME environment variable to reflect the new installation by executing:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-arm64
export PATH=$JAVA_HOME/bin:$PATH
```

- For building Java projects and handling dependencies, I added Maven to the system:

```bash
sudo apt install maven
```

- Gradle installation involved downloading the binary distribution manually and configuring it:

```bash
wget https://services.gradle.org/distributions/gradle-8.6-bin.zip
sudo mkdir /opt/gradle
sudo unzip -d /opt/gradle gradle-8.6-bin.zip
```

- To make Gradle accessible system-wide via the terminal, I appended its path to the environment variables in the .bashrc file:

```bash
echo "export GRADLE_HOME=/opt/gradle/gradle-8.6" >> ~/.bashrc
echo "export PATH=\$GRADLE_HOME/bin:\$PATH" >> ~/.bashrc
source ~/.bashrc
```

With these tools in place, the virtual machine was fully prepared to support the building, execution, and testing of Java-based projects.


## Execute the Spring Boot Tutorial Basic Project

In this part of the assignment, I ran the Spring Boot tutorial basic project, which was a continuation of prior preparatory work. 
The objective was to successfully build and run the application within the virtual machine configured earlier.

1. I navigated to the basic directory, which contains all the necessary files for the Spring Boot application.

2. Inside the project directory, I launched the application using the following command:

```bash
./mvnw spring-boot:run
```

3. To verify external accessibility, I accessed the application through a web browser using the VM’s IP address.
I retrieved the IP address by running the `ifconfig` command in the terminal.
The application was accessible via the following URL:

```
http://192.168.56.5:8080/
```

The application started without issues and displayed the expected content, confirming that the backend was operating correctly and that Spring Boot was successfully serving the application.
To provide evidence of this, I captured a screenshot of the landing page as it appeared in the browser.

<img src=https://i.imgur.com/Az1Zt5Z.png width="800">


## Execute the gradle_basic_demo Project - Part 1

This section outlines the steps I followed to build and run the **gradle_basic_demo project**, which needed to be executed in two separate environments: the virtual machine and the host machine.

1. Within the virtual machine, I accessed the `gradle_basic_demo`.
To compile the project, I ran the following command:

```bash
./gradlew build
```

2. After the project was successfully built, I started the server component of the application by executing:

```bash
./gradlew runServer
```

3. Because the virtual machine was running Ubuntu Server without a graphical user interface, it wasn’t possible to launch GUI-based applications like the chat client directly within the VM. 
To work around this, I switched to the terminal on my host machine, navigated to a local clone of the same project directory, and launched the client component with the following command.
The client was configured to connect to the server running on the VM, using its IP address and designated port:

```bash
./gradlew runClient --args="192.168.56.5 59001"
```

I was able to open two instances of the chat client on the host machine, effectively demonstrating client-server interaction. 
The application performed correctly, with messages being transmitted and received as expected.

<img src=https://i.imgur.com/XKOAjZd.png width="800">

## Execute the gradle_basic_demo Project – Part 2

In this stage of the assignment, I worked on building and running another part of the gradle_basic_demo project from within the virtual machine environment.

1. I navigated to the basic subdirectory within the gradle_basic_demo folder, which is part of the part 3 assignment of CA1.

2. To compile the application and launch the embedded Spring Boot server, I executed the following commands:

```bash
./gradlew build
./gradlew bootRun
```

Once the server started successfully, I opened a web browser and accessed the application using the following URL.
This confirmed that the Spring Boot service was up and running inside the VM and capable of handling HTTP requests from external clients:

```bash
http://192.168.56.5:8080/
```


## Conclusion

This technical report outlined the process of setting up and operating a virtual environment for **Class Assignment 2 Part 1**. using UTM.
The tasks involved provisioning a virtual machine, configuring its network settings and services, and installing the necessary development tools to support software project execution.

The hands-on work with virtualization offered valuable experience in managing virtual machines within a DevOps framework.
Running both the Spring Boot tutorial and the gradle_basic_demo projects within the VM successfully demonstrated the ability to replicate realistic software deployment and runtime scenarios.

One of the main takeaways from this assignment was a stronger grasp of network configuration in virtualized environments, as well as the nuances involved in setting up and maintaining virtual platforms.
Issues such as configuring static IPs and ensuring reliable communication between the host and guest systems were addressed, deepening my understanding of how virtualization behaves in practice.

In summary, this assignment contributed significantly to the development of essential skills for managing complex virtualized infrastructures, and it represents an important step forward in my DevOps training, both academically and professionally.