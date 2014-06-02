@echo off

rem Please edit this variable to contain your JDK executable path 
SET javaPath="\put\jdk\path\here"

rem compile
mkdir bin
%javaPath%\javac -sourcepath src -classpath bin -d bin src\*.java

rem graphics
mkdir bin\images
copy src\images bin\images

rem package
ech . 2>stegnote.jar
%javaPath%\jar ufe stegnote.jar StegNote -C bin .

rem finish
echo "Stegnote has been compiled and packaged into stegnote.jar"
pause
