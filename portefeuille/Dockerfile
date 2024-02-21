FROM maven:3.8.4-openjdk-17-slim

WORKDIR /portefeuille

COPY pom.xml pom.xml
RUN mvn dependency:go-offline

COPY . .

CMD ["mvn", "spring-boot:run"]