#!/bin/sh

MODULENAME="UAPOLLO"
version=`ls ../lib/*_${MODULENAME}_* | awk -F/ '{ print $NF }' | sed -n '1p' | sed -e "s/.war//g"`

echo "        Module: ${MODULENAME}"
echo "System Version: ${version}"