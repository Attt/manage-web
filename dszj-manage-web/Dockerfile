#FROM hub.c.163.com/library/java:8-alpine
FROM hub.c.163.com/housan993/centos7_jdk8

MAINTAINER yys@dishuizhijia.com

ADD target/dszj-manage-web-0.0.1-SNAPSHOT.jar dszj-manage-web.jar

EXPOSE 8080

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN echo 'Asia/Shanghai' >/etc/timezone

ENTRYPOINT ["java", "-jar", "/dszj-manage-web.jar","--spring.profiles.active=dev"]