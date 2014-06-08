#!/bin/sh

#Compile
mkdir -p bin
cp -ur src/images/ bin
javac -g -sourcepath src -classpath bin -d bin src/*.java

#Package
touch stegnote.jar
jar ufe stegnote.jar StegNote -C bin .
