#!/bin/bash

javac -cp .:dist/spool.jar Program.java
java -cp .:dist/spool.jar Program
