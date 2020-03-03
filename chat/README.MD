# Chat service

## Prerequisities
* Java 11
* NPM 10+

## Before first start run command
    gradlew installAngular
    
## How to start
To start frontend and backend on port :8080 run:

    gradlew bootRun

To enable Angular app to live reload on port :4200 navigate to \src\main\webapp and run:
    
    npm start

## How to build executable jar
    gradlew bootJar
    
## Docker
To build a Docker image of your application using Jib connecting to the local Docker daemon:
    
    gradlew -Pprod bootJar jibDockerBuild
    
To build a Docker image of your application without Docker and push it directly into your Docker registry, run:

    gradlew -Pprod bootJar jib