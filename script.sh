#!/bin/bash

for i in 2 4 8
do
    java -cp .:dist/spool.jar Program $i 15 > cv49-P-res-gt-"$i"ser-SRPT.txt
    echo SRPT $i is done
done
