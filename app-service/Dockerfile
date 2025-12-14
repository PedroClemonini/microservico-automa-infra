# Estágio 1: Build (Usando imagem Maven oficial para não depender de arquivos locais)
# Esta etapa SÓ COMPILA e é descartada no final.
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Copia o pom.xml e baixa dependências (Isso cria cache para builds mais rápidos)
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copia o código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests


# Estágio 2: Runtime (Imagem leve apenas com JRE + Terraform)
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app


RUN apt-get update && apt-get install -y wget unzip && rm -rf /var/lib/apt/lists/*


ARG TERRAFORM_VERSION=1.5.7
ARG TERRAFORM_ARCH=amd64
RUN wget https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_${TERRAFORM_ARCH}.zip && \
    unzip terraform_${TERRAFORM_VERSION}_linux_${TERRAFORM_ARCH}.zip && \
    mv terraform /usr/local/bin/ && \
    rm terraform_${TERRAFORM_VERSION}_linux_${TERRAFORM_ARCH}.zip

# 3. Dar permissão de execução
RUN chmod +x /usr/local/bin/terraform

# --- FIM DA INSTALAÇÃO DO TERRAFORM ---

# 4. Copia o JAR gerado e renomeia para um nome padrão 'app.jar'
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta
EXPOSE 8082

# 5. Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
