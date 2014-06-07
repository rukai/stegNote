#!/bin/sh

./compileAndPackage.sh

cd bin
jdb -launch -sourcepath ../src StegNote
