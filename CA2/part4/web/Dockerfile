# Base image com Java 21
FROM openjdk:21-jdk-slim

# Instalar Git e ferramentas necessárias
RUN apt-get update \
    && apt-get install -y git \
    && rm -rf /var/lib/apt/lists/*

# Preparar diretório SSH e copiar chave privada
WORKDIR /root/.ssh
COPY id_rsa /root/.ssh/id_rsa

# Definir permissões e confiar no host do GitHub
RUN chmod 600 /root/.ssh/id_rsa \
    && ssh-keyscan -H github.com >> /root/.ssh/known_hosts

# Clonar o repositório
WORKDIR /usr/src/app
RUN git clone git@github.com:GasparISEP/devops-24-25-1241907.git .

# Ir para o diretório do projeto
WORKDIR /usr/src/app/CA2/part1/part3/react-and-spring-data-rest-basic/

# Dar permissão ao Gradle wrapper e compilar o projeto
RUN chmod +x gradlew \
    && ./gradlew clean build

# Expor a porta da aplicação Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.jar"]