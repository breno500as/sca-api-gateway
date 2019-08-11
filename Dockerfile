#FROM openjdk:8-jdk-alpine as build
#WORKDIR /home/breno/ambiente/git/sca-api-gateway

#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#COPY src src

#RUN ./mvnw install -DskipTests
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/sca-api-gateway-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom  -jar /app.jar" ]