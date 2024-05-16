FROM amd64/amazoncorretto:17

# 패키지 업데이트 및 필요한 도구 설치
RUN yum -y update && \
    yum -y install wget unzip curl && \
    yum clean all

# Chrome 설치
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm && \
    yum -y install ./google-chrome-stable_current_x86_64.rpm && \
    rm -f google-chrome-stable_current_x86_64.rpm

# Chrome WebDriver 설치
RUN CHROME_VERSION=$(google-chrome --version | grep -oP '([0-9]+)') && \
    LATEST_CHROMEDRIVER_VERSION=$(curl -s "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION") && \
    wget -q "https://chromedriver.storage.googleapis.com/$LATEST_CHROMEDRIVER_VERSION/chromedriver_linux64.zip" -O /tmp/chromedriver.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    chmod +x /usr/local/bin/chromedriver && \
    rm /tmp/chromedriver.zip
    
WORKDIR /app

COPY ./build/libs/notify-server-0.0.1-SNAPSHOT.jar /app/notify-server.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "/app/notify-server.jar"]
