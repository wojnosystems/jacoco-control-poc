FROM gradle:jdk8 AS BUILDER

COPY app/build.gradle app/
COPY app/gradle.properties app/
COPY settings.gradle .
COPY app app

RUN gradle test jar

FROM adoptopenjdk/openjdk8:x86_64-debianslim-jdk8u312-b07-slim

WORKDIR /srv/server

COPY --from=BUILDER /home/gradle/app/build/libs/app.jar /srv/server/app.jar

ENTRYPOINT ["java", "-jar", "/srv/server/app.jar"]
