#!/bin/bash

cd /home/pjs/pjs

docker run -d --restart always -p 8080:8080 -v /home/pjs/pjs/webapp:/jar:ro -v /home/pjs/pjs/PJS-DB:/db --name tosho_server amazoncorretto:8 java -server -Dderby.system.home=/db -jar /jar/jetty.jar /jar/ROOT.war
