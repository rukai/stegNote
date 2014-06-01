import java.io.*;
import java.util.*;

class Driver{
        
    private static String printable = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ \t\n\r";
    private static Random random = new Random();

    /**
     * Return a string of a specified length filled with random printable ascii characters.
     */
    private static String generateText(int length){
        String result = "";
        for(int i = 0; i < length; i++){
            result +=  printable.charAt(random.nextInt(printable.length()));
        }
        return result;
    }

    /*
     * Run various tests on the Steganography class
     */
    public static void main(String[] args){
        info();
        boolean result = true;
        long startTime = System.currentTimeMillis();
        
        //predeterminedTests
        String[] predeterminedTests = {
            "",
            "foobarbaz",
            "Here is a human readable sentence",
            "Here is a human readable sentence...\n You will find that, it has some punctuation too!"
        };
        for(int i = 0; i < predeterminedTests.length; i++){
            result = result && runTest(predeterminedTests[i]);
            if(!result) return;
        }

        //all printable characters
        result = result && runTest(printable);
        if(!result) return;

        //lots of small random tests
        for(int i = 0; i < 50; i++){
            result = result && runTest(generateText(2));
            if(!result) return;
        }

        //a few large random tests
        for(int i = 0; i < 10; i++){
            result = result && runTest(generateText(100));
            if(!result) return;
        }

        //one long random test
        long longTestStartTime = System.currentTimeMillis();
        result = result && runTest(generateText(1000));
        if(!result) return;
        long longTestTimeTaken = System.currentTimeMillis() - longTestStartTime;
        
        long timeTaken = (System.currentTimeMillis() - startTime);

        System.out.println("Summary:");
        System.out.println("Various predetermined and random strings of characters of varying lengths were successfully tested.");
        System.out.println("Full time taken: " + Long.toString(timeTaken) + " Milliseconds");
        System.out.println("Time taken for 1000 character test: " + Long.toString(longTestTimeTaken) + " Milliseconds");
    }
    
    /*
     * Display some info to the user
     */
    private static void info(){
        System.out.println("Tested Characters: " + printable);
    }

    /*
     * Run a randomly generated test on the Steganography class
     */
    private static boolean runTest(String input){
        //setup
        String output = "";
        File testFile = new File("foo.bmp");
        
        //input
        Steganography.encode(testFile, input);
        System.out.println("Input:  " + input);
        
        //output
        output = Steganography.decode(testFile);
        System.out.println("Output: " + output);
            
        //result
        boolean result = input.equals(output);
        System.out.println("Result: " +  (result ? "Success" : "Failure") + '\n');
        return result;
    }
}
