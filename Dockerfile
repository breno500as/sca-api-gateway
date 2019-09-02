FROM alpine/git as clone
WORKDIR /app
RUN git clone https://github.com/breno500as/sca-api-gateway.git

FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app
COPY --from=clone /app/sca-api-gateway /app
RUN mvn install -DskipTests
WORKDIR cd /target
ADD target/sca-api-gateway-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]