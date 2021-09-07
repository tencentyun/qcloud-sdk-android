#!/bin/sh

./gradlew :cosxml:clean
./gradlew :cosxml:assemble
./gradlew :cosxml:publishToMavenLocal
#
#cd ../QCloudFoundation
#./gradlew :foundation:clean
#./gradlew :foundation:assemble
#./gradlew :foundation:publishToMavenLocal