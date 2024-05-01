#!/bin/sh
rsync -rlD --exclude='.git' --exclude='.gradle' --exclude='build' /code/ /home/worker
export DISPLAY=:99
Xvfb :99 -screen 0 1024x768x24 &
fluxbox &
/opt/gradle/bin/gradle fest-reflect:check
/opt/gradle/bin/gradle assertj-swing-junit:check
/opt/gradle/bin/gradle assertj-swing-junit-jupiter:check
/opt/gradle/bin/gradle --scan -PenvIsCi=true -Dscan.uploadInBackground=false assertj-swing:check
/opt/gradle/bin/gradle --stop
killall Xvfb
