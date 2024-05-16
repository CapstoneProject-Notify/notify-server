FROM amd64/amazoncorretto:17
    
WORKDIR /app

COPY ./build/libs/notify-server-0.0.1-SNAPSHOT.jar /app/notify-server.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "/app/notify-server.jar"]
