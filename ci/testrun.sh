#!/usr/bin/env bash

CMD="$(type -p docker)" || [[ -e $CMD ]] && $CMD info >/dev/null 2>1 || CMD="$(type -p nerdctl)"
echo select container CLI: $CMD
$CMD info || false

# build container image
IMAGE=miurahr/assertj-swing-test
GRADLE=gradle-8.6
CTX=$(mktemp -d)
cat <<__EOF__ > $CTX/entrypoint.sh
#!/bin/sh
rsync -rlD --exclude='.git' --exclude='.gradle' --exclude='build' /code/ /home/worker
export DISPLAY=:99
Xvfb :99 -screen 0 1024x768x24 &
fluxbox &
/opt/gradle/bin/gradle --no-daemon --scan -PenvIsCi=true -Dscan.uploadInBackground=false clean fest-reflect:check assertj-swing-junit:check assertj-swing-junit-jupiter:check check
killall Xvfb
__EOF__
cat <<__EOF__ > $CTX/Dockerfile
FROM adoptopenjdk/openjdk11:alpine
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
__EOF__
if ! $CMD build -t $IMAGE $CTX ; then
  echo Container build error, abort...
  exit 1
fi

exec $CMD run -i --rm -u `id -u`:`id -g` -v "$PWD":/code $IMAGE
