FROM java:8
MAINTAINER zt
VOLUME /tmp
COPY ./application/application.yml /application/

ADD /target/simple-code-1.0-SNAPSHOT.jar simple-code-1.0-SNAPSHOT.jar
RUN export LC_ALL=zh_CN.UTF-8
RUN echo "export LC_ALL=zh_CN.UTF-8"  >>  /etc/profile
RUN echo "Asia/shanghai" > /etc/timezone
EXPOSE 8083
ENTRYPOINT ["java","-jar","simple-code-1.0-SNAPSHOT.jar","--spring.config.location=/application/application.yml"]
