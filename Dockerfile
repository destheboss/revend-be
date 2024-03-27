FROM gradle:jdk17 as build

WORKDIR /opt/app

COPY ./build/libs/ReVend-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar ReVend-0.0.1-SNAPSHOT.jar"]