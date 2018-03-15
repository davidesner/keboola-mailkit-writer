FROM maven:3-jdk-8

WORKDIR /code
COPY . /code/
RUN mvn compile

ENTRYPOINT mvn -q exec:java -Dexec.args=/data  