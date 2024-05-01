#!/usr/bin/env bash

CMD=nerdctl

IMAGE=miurahr/assertj-swing-test
CTX="$PWD"
if ! $CMD build -t $IMAGE $CTX ; then
  echo Container build error, abort...
  exit 1
fi

exec $CMD run -i --rm -u `id -u`:`id -g` -v "$PWD":/code $IMAGE
