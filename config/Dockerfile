# Start with a base image containing Java runtime
FROM alpine:3.14
RUN apk --no-cache add openjdk11 --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8082 available to the world outside this container
ENV HOST=0.0.0.0
EXPOSE 8202

# environment of app
ENV MYSQL_HOST 127.0.0.1
ENV MYSQL_PORT 3306
ENV MYSQL_DATABASE builders
ENV MYSQL_USER_NAME builders
ENV MYSQL_PASSWORD builders
ENV PROFILES_ACTIVE prod

# The application's jar file
ARG JAR_FILE=ms-cliente-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} ms-cliente.jar

# Run the jar file 
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILES_ACTIVE} -Dmysql.host=${MYSQL_HOST} -Dmysql.port={MYSQL_PORT} -Dmysql.database=${MYSQL_DATABASE} -Dspring.datasource.username=${MYSQL_USER_NAME} -Dspring.datasource.password=${MYSQL_PASSWORD}","-jar","/ms-cliente.jar"]