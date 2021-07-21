#!/bin/sh

./gradlew :cosxml:clean
./gradlew :cosxml:assemble
./gradlew :cosxml:publishMavenAarPublicationToMavenRepository