#!/bin/bash
# Set Process profile
jarfile=$1
profile=$2

# Declare process name.
readonly PROC_NAME="mes-fis-$profile"
# jar Files
readonly DAEMON=$jarfile
# Set up where to place Process ID
readonly PID_PATH="./"
readonly PROC_PID="${PID_PATH}${PROC_NAME}.pid"

start()
{
  echo "nohup java -jar -Dspring.profiles.active=$profile -Dname=${PROC_NAME} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./ -Xms1024m -Xmx2048m -XX:MaxMetaspaceSize=256m -XX:MetaspaceSize=128m -XX:+UseG1GC "${DAEMON}" > /dev/null 2>&1 &"
  nohup java -jar -Dspring.profiles.active=$profile -Dname=${PROC_NAME} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./ -Xms1024 -Xmx2048m -XX:MaxMetaspaceSize=256m -XX:MetaspaceSize=128m -XX:+UseG1GC "${DAEMON}" > /dev/null 2>&1 &
  local PID=$(get_status)
  echo " - Starting..."
  echo " - Created Process ID in ${PROC_PID}"
  echo ${PID} > ${PROC_PID}
}


get_status()
{
    ps ux | grep ${PROC_NAME} | egrep -v "grep|.sh|tail|vim" | awk '{print $2}'
}


start