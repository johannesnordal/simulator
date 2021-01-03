#!/bin/bash

FILENAME=$1
CLASSPATH="-cp .:commons-math3-3.6.1.jar"
javac $CLASSPATH $FILENAME
java $CLASSPATH $(echo "$FILENAME" | cut -f 1 -d '.')
