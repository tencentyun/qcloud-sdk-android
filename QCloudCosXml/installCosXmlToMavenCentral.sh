#!/bin/sh
#
./gradlew :foundation:clean
./gradlew :foundation:assemble
./gradlew :foundation:publishMavenAarPublicationToMavenRepository


#./gradlew :quic:clean
#./gradlew :quic:assemble
#./gradlew :quic:publishMavenAarPublicationToMavenRepository

./gradlew :cos-android:clean
./gradlew :cos-android:assemble
./gradlew :cos-android:publishMavenAarPublicationToMavenRepository

