#!/bin/sh

#./gradlew :foundation:clean
#./gradlew :foundation:assemble
#./gradlew :foundation:publishToMavenLocal

./gradlew :cos-android:clean
./gradlew :cos-android:assemble
./gradlew :cos-android:publishToMavenLocal
