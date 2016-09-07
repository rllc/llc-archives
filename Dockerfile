FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER "Steven McAdams <steven.mcadams@target.com>"
EXPOSE 8080
VOLUME /tmp
ADD app.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
