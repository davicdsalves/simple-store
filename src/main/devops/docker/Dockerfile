FROM openjdk:8u131-jdk-alpine

RUN unlink /etc/localtime && ln -s /usr/share/zoneinfo/Europe/Berlin /etc/localtime
RUN rm -rf /tmp/* /var/tmp/*

VOLUME /tmp
ADD store*.jar /app/app.jar

WORKDIR /app
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
