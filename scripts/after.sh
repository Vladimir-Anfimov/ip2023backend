#!/bin/bash

cd /home/backend

mvn clean && mvn compile && mvn test && mvn package && mvn install

cd target

nohup java -jar dealshopper*SNAPSHOT.jar &