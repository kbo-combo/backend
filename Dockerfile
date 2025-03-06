FROM --platform=linux/arm64 openjdk:21-jdk-slim

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

WORKDIR /app

COPY build/libs/kbo-combo-0.0.1-SNAPSHOT.jar app.jar

ARG SPRING_PROFILE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILE

EXPOSE 8080

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
