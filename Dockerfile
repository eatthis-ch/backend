FROM maven:3-openjdk-11-slim as builder

# Prepare env
WORKDIR /app

# Get Source Code into image
COPY . .

# Build process
RUN mvn dependency:go-offline -B
RUN mvn package

############# Final image #############
FROM openjdk:11-jre-slim-bullseye

# non-root stuff
RUN addgroup jre --system --gid 1000
RUN adduser --system jre --home /home/jre/ --shell /bin/bash --uid 1000 --gid 1000
USER jre 
WORKDIR /home/jre/

# App binary
COPY --from=builder /app/target/backend-*.jar backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/home/jre/backend.jar"]
