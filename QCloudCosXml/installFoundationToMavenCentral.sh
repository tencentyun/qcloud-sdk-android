#!/bin/sh

./gradlew :foundation:clean
./gradlew :foundation:assemble
./gradlew :foundation:publishMavenAarPublicationToMavenRepository