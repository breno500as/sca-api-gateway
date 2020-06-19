FROM openjdk:8-jdk-alpine as runtime
VOLUME /tmp
COPY target/sca-api-gateway-exec.jar app.jar
ENTRYPOINT ["java", "$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]