#!/bin/sh

mkdir -p bin
javac -sourcepath src -classpath bin -d bin src/*.java
cp -ur src/images/ bin
