FROM eclipse-temurin:21-alpine

ARG GITHUB_SHA
ENV GITHUB_SHA=${GITHUB_SHA}

EXPOSE 8080
ADD backend/target/theworkofart.jar theworkofart.jar
ENTRYPOINT ["java", "-jar", "theworkofart.jar"]
