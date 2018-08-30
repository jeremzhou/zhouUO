#!/usr/bin/python

import urllib2
import os
import sys
import time
import subprocess
import commands
import logging
import json


def parseStatus(content):
    jsonMap = json.loads(content)
    serviceStatus = jsonMap.get('serviceStatus')
    return serviceStatus if serviceStatus != None else -1


def getIpPort(confFile):
    ip = '127.0.0.1'
    port = 9999
    count = 0
    file = open(confFile)
    line = file.readline()
    while line:
        arrs = line.replace("\r\n", "").replace("\n", "").split(":")
        if (arrs[0] == "  address"):
            ips = arrs[1].strip().split(" ")
            ip = ips[0].strip()
            if ip == '0.0.0.0':
                ip = '127.0.0.1'
            count += 1
        if (arrs[0] == "  port"):
            ports = arrs[1].strip().split(" ")
            port = ports[0].strip()
            count += 1
        if count == 2:
            break
        line = file.readline()
    file.close()
    return ip, port


def postHeartBeat(url, headers, data):
    postReq = urllib2.Request(url=url, headers=headers, data=data)
    try:
        response = urllib2.urlopen(postReq, timeout=10)
        status = response.getcode()
        if (status != 200):
            logging.error("postHeartBeat the status: %d" % (status))
            return -1
        else:
            return parseStatus(response.read())
    except Exception, e:
        logging.error("postHeartBeat generate exception: %s" % (e))
        return -1


def restartUapollo(uapolloHome):
    os.chdir(uapolloHome + "/bin")
    logging.info("start to run restartUapollo. curr dir: %s" % (os.getcwd()))
    subprocess.Popen('nohup ./restart_uapollo.sh &', shell='true')
    logging.info("end to run restartUapollo. cur dir: %s" % (os.getcwd()))
    sys.exit()


def runTest(postUrl, postHeaders, postCotent):
    exceptCount = 0
    while True:
        if (postHeartBeat(postUrl, postHeaders, postCotent) == 0):
            exceptCount = 0
        else:
            exceptCount += 1;
            logging.info("postHeartBeat exception, exceptCount: %s" % (exceptCount))

        uapolloCount = commands.getoutput(
            "ps -ef | grep UAPOLLO | grep '\.war$' | grep -v grep | wc -l")
        if (exceptCount > 6 or uapolloCount != "1"):
            logging.info(
                "monitor uapollo run excepted, so restartUapollo. exceptCount: %s uapolloCount: %s" % (
                    exceptCount, uapolloCount))
            restartUapollo(uapolloHome)

        time.sleep(10)


# set log format
logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)-8s %(levelname)-5s %(message)s')

if __name__ == "__main__":

    logging.info("start to run uapollo_monitor.")
    logging.info("first, sleep 300 seconds...")
    time.sleep(300)
    logging.info("sleep compeleted, continue...")

    uapolloHome = os.getenv("UAPOLLO_HOME")
    if (not os.path.exists(uapolloHome)):
        logging.info(
            "uapollo run dir: don't exists! uapollo_monitor exit!" % (uapolloHome))
        sys.exit()
    logging.info("uapollo home: %s" % (uapolloHome))

    confFile = uapolloHome + "/config/uapollo.yml"
    (ip, port) = getIpPort(confFile)
    postUrl = "http://" + ip + ":" + port + "/api/v1/neheartbeat"
    logging.info("uapollo heartbeat post url: %s" % (postUrl))
    postHeaders = {'Content-Type': 'application/json'}
    postCotent = '{"message": "NeHeartBeat"}'
    runTest(postUrl, postHeaders, postCotent)
