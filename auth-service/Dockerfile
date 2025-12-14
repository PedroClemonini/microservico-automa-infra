# Estágio 1: Build (Compilação)
# Usamos o Temurin 21 SDK (Development Kit) para compilar
FROM eclipse-temurin:21-jdk-jammy AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o código-fonte e o arquivo de construção
COPY pom.xml .
COPY src src
# Assume que você tem o Maven Wrapper (mvnw) no diretório raiz
COPY mvnw .
COPY .mvn .mvn

# Torna o Maven Wrapper executável
RUN chmod +x mvnw

# Executa o build do projeto (Maven)
# O comando 'mvn package' criará o JAR executável (fat JAR) em target/
RUN ./mvnw clean package -DskipTests

# Estágio 2: Runtime (Execução)
# Usamos o Temurin 21 JRE (Runtime Environment), mais leve, para a produção
FROM eclipse-temurin:21-jre-jammy

# Define o diretório onde o JAR será colocado
WORKDIR /app

# Copia o JAR executável do estágio 'builder' para o estágio 'runtime'
# Você precisará confirmar o nome exato do seu JAR
COPY --from=builder /app/target/*.jar auth-service.jar

# Expõe a porta que o serviço Spring Boot utiliza (8081)
EXPOSE 8081

# Comando para iniciar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
