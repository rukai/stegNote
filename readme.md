# StegNote

This is a simple Steganography application written in java for my HSC major work.

## Usage

Run stegnote.jar and use the software like a text editor:

1.  Open a BMP file to modify.
2.  Use the textbox to read, modify or create a message.
3.  Press save to make the changes permanent.

## Building


### Requirements:

This process has been tested to work with JDK 7, however any recent JDK should work.

### Unix

Run the following in the project directory.

    ./compile.sh
    ./package.sh

### Windows

Open the file 'compile.bat' in a text editor.

Locate the line:

    SET javaPath="\put\jdk\path\here"

And modify it to contain the path to your JDK bin directory.
This will be different for every computer, but here is an example:

    SET javaPath="\Program Files\Java\jdk1.7.0_51\bin"

Now run the 'compile.bat' script.
