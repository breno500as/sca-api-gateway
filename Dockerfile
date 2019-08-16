FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/sca-api-gateway-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom  -jar /app.jar" ]