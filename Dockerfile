# Ref: https://www.adevguide.com/dockerize-java-application-maven-with-dockerfile/
FROM maven:3-openjdk-11-slim as builder

# Prepare env
WORKDIR /app

# Get Source Code into image
COPY . .

# Build process
RUN mvn package -Dmaven.test.skip=true 

############# Final image #############
FROM openjdk:11-jre-slim-bullseye

# non-root stuff
RUN addgroup spring --system --gid 1000
RUN adduser spring --system  --home /home/spring --shell /bin/bash --uid 1000 --gid 1000
USER spring:spring 
WORKDIR /home/spring

# App binary
COPY --from=builder /app/target/backend-*.jar backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/home/spring/backend.jar"]
