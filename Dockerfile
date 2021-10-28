FROM docker.io/openjdk:8-jdk-alpine AS builder

ENV MAVEN_VERSION 3.5.4
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$
WORKDIR /app

# Get maven
RUN wget http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
 tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
 rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
 mv apache-maven-$MAVEN_VERSION $MAVEN_HOME

# Get dependencies
COPY pom.xml .
RUN mvn install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

############# Final image #############
FROM docker.io/openjdk:8-jdk-alpine
LABEL MAINTAINER technat@technat.ch

### non-root stuff
RUN addgroup -S spring && adduser -S pring -G spring -h /app
USER spring:spring
WORKDIR /app

## App 
ARG DEPENDENCY=/app/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080

ENTRYPOINT ["java","-cp","app:app/lib/*","backend.Application"]
