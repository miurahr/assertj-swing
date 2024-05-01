FROM adoptopenjdk/openjdk11:alpine
ARG GRADLE=gradle-8.6
USER root
RUN apk add --no-cache xvfb xvfb-run libsm-dev libxrender libxext-dev libxtst-dev libxcb-dev ttf-dejavu \
 font-xfree86-type1 font-sun-misc fluxbox rsync curl
RUN adduser --disabled-password --gecos "" --home /home/worker --shell /bin/bash worker && mkdir -p /home/worker/ \
 && mkdir -p /usr/local/bin/ && chown -R worker /home/worker
RUN (cd /opt && curl -LO https://services.gradle.org/distributions/gradle-8.6-bin.zip \
 && unzip -q ${GRADLE}-bin.zip && rm -f ${GRADLE}-bin.zip) && mv /opt/${GRADLE} /opt/gradle
COPY entrypoint.sh /usr/local/bin/
RUN chmod 755 /usr/local/bin/entrypoint.sh

USER worker
WORKDIR /home/worker
ENTRYPOINT /usr/local/bin/entrypoint.sh
