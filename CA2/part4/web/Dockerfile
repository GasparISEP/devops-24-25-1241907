FROM tomcat:10.1-jdk21

# Instalar dependências básicas
RUN apt-get update && apt-get install -y git curl npm && rm -rf /var/lib/apt/lists/*

# Preparar diretório SSH e chave
WORKDIR /root/.ssh
COPY id_rsa /root/.ssh/id_rsa
RUN chmod 600 id_rsa && ssh-keyscan -H github.com >> known_hosts

# Clonar repositório
WORKDIR /usr/src/app
RUN git clone git@github.com:GasparISEP/devops-24-25-1241907.git .

# ENTRAR no projeto onde está o React e o Spring
WORKDIR /usr/src/app/CA2/part1/part3/react-and-spring-data-rest-basic

# Construir o frontend React
RUN npm install
RUN npm run build  # ou npm run webpack, se for o que deseja invocar

# Compilar o backend com o frontend embutido
ENV GRADLE_USER_HOME=/usr/src/app/.gradle
RUN chmod +x gradlew
RUN ./gradlew clean bootWar

# Copiar o WAR gerado para o Tomcat
RUN cp build/libs/basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/basic-0.0.1-SNAPSHOT.war

EXPOSE 8080