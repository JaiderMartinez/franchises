FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY **/app-service/build/libs/*.jar franchises.jar
ENV JAVA_OPTS=" -Duser.timezone=America/Bogota"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar franchises.jar" ]
