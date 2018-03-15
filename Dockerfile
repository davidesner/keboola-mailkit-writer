FROM maven:3-jdk-8
MAINTAINER David Esner <esnerda@gmail.com>

ENV APP_VERSION 1.1.0

# set switch that enables correct JVM memory allocation in containers
ENV JAVA_OPTS "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
ENV MAVEN_OPTS "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"

 WORKDIR /home
RUN git clone https://github.com/davidesner/keboola-mailkit-writer.git ./  
RUN mvn compile

ENTRYPOINT mvn -q exec:java -Dexec.args=/data  