FROM gradle:jdk17 AS build

WORKDIR /opt/app

COPY ./build/libs/ReVend-0.0.1-SNAPSHOT.jar ./

RUN useradd -m appuser

USER appuser

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar ReVend-0.0.1-SNAPSHOT.jar"]