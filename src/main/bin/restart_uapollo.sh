#!/bin/bash

source ./setEnv.sh
SHELLNAME=`basename $0`

printLog "$SHELLNAME" "stop uapollo ..."
./stop_uapollo.sh
printLog "$SHELLNAME" "stop uapollo success! start the uapollo ..."
nohup ./start_uapollo.sh &
printLog "$SHELLNAME" "start uapollo completed, please use 'tail -f nohup.out' to check start result."
