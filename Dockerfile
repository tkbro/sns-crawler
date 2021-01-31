### STAGE 1: Build ###
# jdk8 has a bug. Ref: https://bugs.openjdk.java.net/browse/JDK-8067747
FROM maven:3.6.3-openjdk-8-slim as builder

COPY --chown=maven:maven . /sns-crawler
WORKDIR /sns-crawler
RUN mvn clean package -Dmaven.test.skip=true

### STAGE 2: Setup ###
FROM openjdk:8-jre-slim

RUN apt-get update && apt-get install -y gnupg gnupg2 wget unzip

# Chrome
ARG CHROME_VERSION=88.0.4324.96-1
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
	&& echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
	&& apt-get update -qqy \
	&& apt-get -qqy install google-chrome-stable=$CHROME_VERSION \
	&& rm /etc/apt/sources.list.d/google-chrome.list \
	&& rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
	&& sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome

# ChromeDriver
ARG CHROME_DRIVER_VERSION=88.0.4324.96
RUN wget -q -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$CHROME_DRIVER_VERSION/chromedriver_linux64.zip \
	&& unzip /tmp/chromedriver.zip -d /opt \
	&& rm /tmp/chromedriver.zip \
	&& mv /opt/chromedriver /opt/chromedriver-$CHROME_DRIVER_VERSION \
	&& chmod 755 /opt/chromedriver-$CHROME_DRIVER_VERSION \
	&& ln -s /opt/chromedriver-$CHROME_DRIVER_VERSION /usr/bin/chromedriver

COPY --from=builder /sns-crawler/target/sns-crawler*.jar /sns-crawler/sns-crawler.jar
WORKDIR /sns-crawler

EXPOSE 8080

CMD ["java", "-jar", "/sns-crawler/sns-crawler.jar"]
