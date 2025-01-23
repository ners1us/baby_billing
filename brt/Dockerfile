FROM maven:3.8.4-openjdk-17 AS build
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-oracle
EXPOSE 8082
ARG JAR_FILE=/build/target/*.jar
COPY --from=build /build/target/brt-1.0.2.jar brt.jar
ENTRYPOINT ["java", "-jar", "brt.jar"]
