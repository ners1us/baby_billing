FROM openjdk:17-jdk-oracle
ARG JAR_FILE=target/*.jar
COPY ./target/baby_billing-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8070
ENTRYPOINT ["java", "-jar", "app.jar"]
