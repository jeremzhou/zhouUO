#!/bin/bash

source ./setEnv.sh
SHELLNAME=`basename $0`

printLog "$SHELLNAME" "start to run uapollo."

if [ `ps -ef | grep "UAPOLLO" | grep "\.war$" | grep -v grep | wc -l` -eq "1" ]; then
    printLog "$SHELLNAME" "the uapollo has been running. exit!"
    exit 0
fi

cd ..
UAPOLLO_HOME=`pwd`
export UAPOLLO_HOME

if [ `ps -ef | grep "uapollo_monitor.py" | grep -v grep | wc -l` -eq "0" ]; then
    printLog "$SHELLNAME" "run uapollo_monitor.py script."
    cd ./bin
    nohup ./uapollo_monitor.py &
    cd ..
fi

WAR_FILE=`ls ./lib/UOMC*UAPOLLO* | sed -n '1p'`

[ -d ${UAPOLLO_HOME}/logs/gc ] || mkdir -p ${UAPOLLO_HOME}/logs/gc
if [ $? -ne "0" ]; then
        printLog "$SHELLNAME" "create gc log dir : ${UAPOLLO_HOME}/logs/gc failed! exit!"
        exit 2
fi

printLog "$SHELLNAME" "run uapollo main program."
printLog "$SHELLNAME" "uapollo used memory size: ${JAVAMEM}"
CURRENT_DATE=`date '+%Y%m%d-%H.%M.%S'`
GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:MaxDirectMemorySize=128M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${UAPOLLO_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"
#performance stable, startup slowly for -XX:+AlwaysPreTouch
#GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:MaxDirectMemorySize=1024M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${UAPOLLO_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"

$JAVA $GC_OPTS -Djava.net.preferIPv4Stack=true -Dspring.property.path="${UAPOLLO_HOME}" -Dlogging.config=file:"${UAPOLLO_HOME}"/config/logback.xml -jar ${WAR_FILE}
