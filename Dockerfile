FROM openjdk:21-jdk-slim

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

WORKDIR /app

COPY build/libs/kbo-combo-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
