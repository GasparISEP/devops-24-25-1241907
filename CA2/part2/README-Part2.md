# CA2: Part 2 - Virtualization with Vagrant

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

## Table of Contents

- [Introduction](#introduction)
- [Environment Setup](#environment-setup)
- [Setup Base Project](#setup-base-project)
- [Vagrantfile](#vagrantfile)
- [Connecting Spring Boot to H2 Database](#connecting-spring-boot-to-h2-database)
- [Running the Project](#running-the-project)
- [Vagrant commands](#vagrant-commands)
- [Alternative Solution](#alternative-solution)
- [Conclusion](#conclusion)


## Introduction

This technical report presents the implementation process and results of **Class Assignment 2 - Part 2**, which focused on virtualization using Vagrant.
The task involved setting up a virtualized environment capable of running a Spring Boot application integrated with an H2 database.
This report outlines the full configuration workflow, including the setup of the Vagrant environment, the integration of the Spring Boot backend with the H2 database, and the successful execution of the project.
Moreover, it includes a comparison between QEMU (used in this assignment) and VirtualBox, discussing the main distinctions between both virtualization platforms.


## Environment Setup

**Prerequisites Setup**

To create a virtualized development environment with Vagrant on a macOS device powered by an M1 or M2 chip, I followed the steps below.
Due to compatibility issues with VirtualBox on ARM architecture, I opted for QEMU as the virtualization provider.

Before installing the necessary tools, I ensured that both Xcode Command Line Tools and Homebrew were available on the system.

To install Homebrew:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

**Install QEMU and libvirt**
These are required to enable Vagrant to work with virtual machines on ARM architecture.

```bash
brew install qemu
brew install libvirt
```

**Install Vagrant and Required Plugins**
I used the official Vagrant package from HashiCorp for better reliability:

```bash
brew install hashicorp/tap/hashicorp-vagrant
```

Then, I installed the necessary plugin to allow Vagrant to work with QEMU:

```bash
vagrant plugin install vagrant-qemu
```

**Initialize the Vagrant Environment**
Once all the dependencies were installed, I created a local project directory and placed the required Vagrant files inside.
To start the virtual environment with the correct networking setup, I used:

```bash
sudo vagrant up
```

The use of sudo is essential to apply the virtual machine’s network configuration properly under QEMU.

**.gitignore Configuration**
To keep the version control repository clean and free from build artifacts or machine-specific files, I updated the .gitignore file with the following entries:

```bash
.vagrant/
*.war
```


## Vagrantfile

The Vagrantfile serves as the blueprint for defining the settings and provisioning steps of the virtual machines.
After generating the base configuration, I applied several important adjustments to better align it with the specific needs of this project:

1.	**Updated the Git Repository:** I replaced the default repository URL with the link to my own project repository to ensure the correct source code was fetched.
2.	**Adjusted the Project Path:** I corrected the directory structure within the provisioning script so that the path accurately reflected the layout of my application files.
3.	**Included the bootRun Execution Step:** To simplify running the Spring Boot application, I included the ./gradlew bootRun command as part of the provisioning routine.
4.	**Modified the Java Version:** I configured the environment to use OpenJDK 21 instead of the original version to ensure compatibility with the project’s requirements.

Here's the updated Vagrantfile:

```ruby
Vagrant.configure("2") do |config|
    config.ssh.forward_agent = true
  config.vm.box = "perk/ubuntu-2204-arm64"

  # This provision is common for both VMs
  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get -y update
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
         openjdk-21-jdk-headless
    # ifconfig
  SHELL

  #============
  # Configurations specific to the database VM
  config.vm.define "db" do |db|
    db.vm.box = "perk/ubuntu-2204-arm64"
    db.vm.hostname = "db"

    db.vm.provider "qemu" do |qe|
      qe.arch = "aarch64"
      qe.machine = "virt,accel=hvf,highmem=off"
      qe.cpu = "cortex-a72"
      qe.net_device = "virtio-net-pci"
      qe.memory = "512"
      qe.ssh_port = 50122
      qe.extra_qemu_args = %w(-netdev vmnet-host,id=vmnet,start-address=192.168.56.1,end-address=192.168.56.255,subnet-mask=255.255.255.0 -device virtio-net-pci,mac=52:54:00:12:34:50,netdev=vmnet)
    end

    # We want to access H2 console from the host using port 8082
    # We want to connet to the H2 server using port 9092
    db.vm.network "forwarded_port", guest: 8082, host: 8082
    db.vm.network "forwarded_port", guest: 9092, host: 9092

    # We need to download H2 and configure host-network
    config.vm.provision "shell", inline: <<-SHELL
      wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar
    SHELL

    # The following provision shell will run ALWAYS so that we can execute the H2 server process
    # This could be done in a different way, for instance, setiing H2 as as service, like in the following link:
    # How to setup java as a service in ubuntu: http://www.jcgonzalez.com/ubuntu-16-java-service-wrapper-example
    #
    # To connect to H2 use: jdbc:h2:tcp://192.168.33.11:9092/./jpadb
    db.vm.provision "file", source: "provision/netcfg-db.yaml", destination: "/home/vagrant/01-netcfg.yaml"
    db.vm.provision "shell", :run => 'always', inline: <<-SHELL
      sudo mv /home/vagrant/01-netcfg.yaml /etc/netplan
      chmod 600 /etc/netplan/01-netcfg.yaml
      sudo netplan apply

      java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists > ~/out.txt &
    SHELL
  end

  #============
  # Configurations specific to the webserver VM
  config.vm.define "web" do |web|
    web.vm.box = "perk/ubuntu-2204-arm64"
    web.vm.hostname = "web"

    web.vm.provider "qemu" do |qe|
      qe.arch = "aarch64"
      qe.machine = "virt,accel=hvf,highmem=off"
      qe.cpu = "cortex-a72"
      qe.net_device = "virtio-net-pci"
      qe.memory = "1G"
      qe.ssh_port = 50222
      qe.extra_qemu_args = %w(-netdev vmnet-host,id=vmnet,start-address=192.168.56.1,end-address=192.168.56.255,subnet-mask=255.255.255.0 -device virtio-net-pci,mac=52:54:00:12:34:51,netdev=vmnet)
    end

    # We want to access tomcat from the host using port 8080
    web.vm.network "forwarded_port", guest: 8080, host: 8080

    web.vm.provision "file", source: "provision/netcfg-web.yaml", destination: "/home/vagrant/01-netcfg.yaml"
    web.vm.provision "shell", inline: <<-SHELL, privileged: false
      sudo mv /home/vagrant/01-netcfg.yaml /etc/netplan
      chmod 600 /etc/netplan/01-netcfg.yaml
      sudo netplan apply

      #sudo apt-get install git -y
      #sudo apt-get install nodejs -y
      #sudo apt-get install npm -y
      #sudo ln -s /usr/bin/nodejs /usr/bin/node
      sudo apt install -y tomcat9 tomcat9-admin
      # If you want to access Tomcat admin web page do the following:
      # Edit /etc/tomcat9/tomcat-users.xml
      # uncomment tomcat-users and add manager-gui to tomcat user

      # Change the following command to clone your own repository!
      ssh-keyscan -H github.com >> ~/.ssh/known_hosts
      git clone git@github.com:GasparISEP/devops-24-25-1241907.git
      cd devops-24-25-1241907/CA2/part1/part3/react-and-spring-data-rest-basic/
      chmod u+x gradlew
      ./gradlew clean build
      ./gradlew bootRun
      # To deploy the war file to tomcat9 do the following command:
      sudo cp ./build/libs/basic-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps
    SHELL
  end
end
```


## Connecting Spring Boot to H2 Database

To establish a connection between the Spring Boot application and the H2 database, I made several targeted updates within the `react-and-spring-data-rest-basic` project.

**Editing application.properties**
In order to configure the application’s connection to the H2 database, I updated the `src/main/resources/application.properties` file with the following settings:

```properties
server.servlet.context-path=/basic-0.0.1-SNAPSHOT
spring.data.rest.base-path=/api
spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=create
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true
```

These properties ensure that the application can communicate with the H2 server running in the database VM, while also enabling the web console for easier debugging and inspection.

**Updating the React Application**
To reflect the adjusted context and API paths defined in the Spring Boot configuration, the React frontend was modified accordingly.
In `src/App.js`, the client call was updated as follows:

```javascript
client({method: 'GET', path: '/basic-0.0.1-SNAPSHOT/api/employees'}).done(response => {
```


## Running the Project

Prior to launching the project, I confirmed that VirtualBox was properly set up and that the repository intended for cloning was publicly accessible.
After that, I accessed the corresponding project directory and ran the following command:

```bash
sudo vagrant up
```

This command launched the virtual machines and configured them based on the specifications defined in the Vagrantfile.

Once the virtual environments were active, I accessed http://localhost:8080/basic-0.0.1-SNAPSHOT/ through my web browser to verify whether the Spring Boot application had been deployed successfully.
The screenshot below shows the outcome:

<img src="https://i.imgur.com/NoCG65t.png" width="800"/>


To verify the database connection, I accessed the H2 console at http://localhost:8082/h2-console and connected to the H2 database using the JDBC URL jdbc:h2:tcp://192.168.56.11:9092/./jpadb.
After establishing the connection, I was able to view the available tables and their data.
The screenshot below shows the H2 console displaying the EMPLOYEE table, confirming that the Spring Boot application was functioning correctly and successfully communicating with the H2 database:

<img src="https://i.imgur.com/tKpoRBD.png" width="800"/>


These actions validated that the Spring Boot application was operating correctly and was able to interact with the H2 database without issues.


## Alternative Solution

In this section, I explore VMware and QEMU as two distinct virtualization solutions suitable for integrating with Vagrant.
The comparison below highlights their respective advantages and challenges, followed by implementation details for using VMware in a Vagrant environment.

**Comparison of VMware and QEMU**

- **VMware:**(Workstation and Fusion)

  - **Overview**: A premium-grade virtualization tool designed for professional and enterprise use, offering strong performance and a broad feature set.
  
  - **Advantages**:
    - Excellent performance and stability under demanding workloads.
    - Advanced tools such as cloning, snapshots, and shared VMs.
    - Seamless compatibility with VMware’s broader ecosystem.
  
  - **Drawbacks**:
    - Requires a commercial license after the trial.
    - More complex to fully utilize advanced enterprise-level capabilities.

- **QEMU (Quick Emulator):**
  - **Overview**:  A powerful and fully open-source virtualization and emulation platform capable of running a variety of architectures.
  Often used alongside **KVM** (Kernel-based Virtual Machine) for enhanced performance on Linux systems.

  - **Advantages**:
    - Completely free and open-source.
    - Can emulate different CPU architectures (ideal for cross-platform testing).
    - Efficient when combined with KVM, delivering near-native performance.
    - Suitable for automated and headless environments.
  
  - **Drawbacks**:
    - Lacks a built-in GUI, requiring command-line interaction.
    - Steeper learning curve for initial setup.
    - Integration with Vagrant requires extra tools such as libvirt and vagrant-libvirt.

**Using VMware with Vagrant**

To use VMware as the virtualization provider with Vagrant, the following steps are required:

1. Install the VMware Utility for Vagrant
This component allows Vagrant to control VMware virtual machines.

```bash
https://releases.hashicorp.com/vagrant-VMware-utility/1.0.14/vagrant-VMware-utility_1.0.14_x86_64.deb
sudo dpkg -i vagrant-VMware-utility_1.0.14_x86_64.deb
```

2. Install the VMware Plugin for Vagrant
This plugin bridges Vagrant with VMware Workstation or Fusion.

```bash
vagrant plugin install vagrant-VMware-desktop
```

3. Edit the Vagrantfile
Define VMware as the provider and specify VM settings as needed.

```ruby
Vagrant.configure("2") do |config|
  config.vm.box = "hashicorp/bionic64"
  config.vm.provider "VMware_desktop" do |v|
    v.vmx["memsize"] = "1024"
    v.vmx["numvcpus"] = "2"
  end
end
```

Both VMware and QEMU provide robust virtualization capabilities, though they cater to different use cases.

- VMware is ideal for users seeking performance, advanced features, and seamless integration with a graphical interface.

- QEMU, especially when paired with KVM, is a flexible and lightweight option well-suited for headless environments or projects requiring architecture emulation.

The choice between them should be guided by the specific needs of the development workflow, resource availability, and licensing constraints.


## Conclusion

This technical report outlines the process and outcomes of **Class Assignment 2 - Part 2**, which focused on implementing virtualization using Vagrant.
The setup involved configuring the Vagrant environment, connecting a Spring Boot application to an H2 database, and successfully running the project — showcasing the practical application of virtualization techniques.
Furthermore, an alternative approach using VMware with Vagrant was explored, comparing it with QEMU to highlight the differences in performance, compatibility, and the specific benefits that VMware can offer for more advanced virtualization use cases.