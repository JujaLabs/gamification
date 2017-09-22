FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Danil Kuznetsov <kuznetsov.danil.v@gmail.com>

ENV LANG="en_US.UTF-8" \
    LC_ALL="en_US.UTF-8" \
    LANGUAGE="en_US.UTF-8" \
    TERM="xterm"
ENV TIMEZONE="Europe/Kiev"

RUN apk update && \
    apk upgrade && \
    apk add --update tzdata && \
    cp /usr/share/zoneinfo/${TIMEZONE} /etc/localtime && \
    echo "${TIMEZONE}" > /etc/timezone

RUN apk del tzdata && \
    rm -rf /var/cache/apk/*

WORKDIR /data/
ADD target/gamification-0.1.0.jar /data/app.jar
RUN sh -c 'touch /data/app.jar'

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Duser.timezone=Europe/Kiev -jar /data/app.jar" ]