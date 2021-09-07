#!/bin/sh

./gradlew :cosxml:clean
./gradlew :cosxml:assemble
./gradlew :cosxml:publishMavenAarPublicationToMavenRepository

cd ../QCloudFoundation
./gradlew :foundation:clean
./gradlew :foundation:assemble
./gradlew :foundation:publishMavenAarPublicationToMavenRepository