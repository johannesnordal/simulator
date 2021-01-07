#!/bin/bash

FILENAME=$1

if [ -z "$FILENAME" ]
then
    FILENAME="MainController.java"
fi

javac -d ../bin *.java
java -cp .:../bin spool.$(echo "$FILENAME" | cut -f 1 -d '.')
