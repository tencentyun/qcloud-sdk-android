#!/bin/bash
emulator=`adb devices | grep emulator`



if [[ -z $emulator ]]
then
   echo "返回 true"
   exit 1
else
  echo "错误"
fi


