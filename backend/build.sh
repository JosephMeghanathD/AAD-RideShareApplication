#!/bin/sh

cd ./RideShareApp || exit
./gradlew clean build publishToMavenLocal
cd ..


cd ./RideShareAppAuthService || exit
./gradlew clean build
cd ..

cd ./RideShareAppChatService || exit
./gradlew clean build
cd ..

cd ./RideShareAppRideService || exit
./gradlew clean build
cd ..


docker-compose down --rmi all --volumes --remove-orphans auth-service ride-service chat-service
docker-compose build
docker-compose up -d
