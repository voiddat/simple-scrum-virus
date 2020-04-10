FROM openjdk:8-jdk-alpine
WORKDIR /opt/app
ARG API_JAR=target/simple-scrum-virus-0.0.1-SNAPSHOT.jar
COPY ${API_JAR} scrum-virus-api.jar
ENTRYPOINT ["java","-jar","scrum-virus-api.jar"]
