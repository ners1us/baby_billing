FROM maven:3.8.4-openjdk-17 AS build
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-oracle
EXPOSE 8083
ARG JAR_FILE=/build/target/*.jar
COPY --from=build /build/target/hrs-0.0.1-SNAPSHOT.jar hrs.jar
ENTRYPOINT ["java", "-jar", "hrs.jar"]
