import java.io.*;

/*
 * LSB Steganography reading and writing.
 * all encoded messages begin with a series of 32 bits containing 0's
 * this gives a 1 in 4,294,967,296 chance of reading a non message as a message.
 */
class Steganography{
    /*
     * Takes a file and payload message.
     * Using only the LSB of each byte writes 32 bytes of 0's as proof that the image has a message hidden.
     * The message is then written starting from the next byte using only the LSB from each byte.
     * A final byte of 0's is added after the message as a sentinel value.
     */
    public static void encode(File carrier, String payload){
        int pos = locatePixelArray(carrier);
        int readByte = 0;
        try(RandomAccessFile stream = new RandomAccessFile(carrier, "rw")){
            //set the LSB for the first 32 bytes as 0
            stream.seek(pos);
            for(int i = 0; i < 32; i++){
                readByte = stream.read();
                stream.seek(pos);
                stream.write(readByte & 0b11111110);
                pos++;
            }

            //write payload to carrier
            payload += (char) 0;
            int payloadByte;
            int payloadBit;
            int newByte;
            for(char element : payload.toCharArray()){
                payloadByte = (int) element;
                //System.out.println(element + ":" + Integer.toString(character)); //uncomment for debug
                for(int i = 0; i < 8; i++){
                    readByte = stream.read();
                    payloadBit = (payloadByte >> i) & 1;
                    newByte = (readByte & 0b11111110) | payloadBit;
                    stream.seek(pos);
                    stream.write(newByte);
                    pos++;
                }
            }

        } catch(IOException e){
            return;
        }
    }
    /*
     * Find the location of the pixel array of a BMP image.
     */
    public static int locatePixelArray(File file){
        try(FileInputStream stream = new FileInputStream(file)){
            //skip to section in header specifying location
            stream.skip(10);
            //read 4 bytes together as an integer to find the offset of the pixel array
            int location = 0;
            for(int i = 0; i < 4; i++){
                location = location | (stream.read() << (4 * i));
            }
            return location;
        } catch(IOException e){
            return -1;
        }
    }
    
    /*
     * Takes a file and returns the hiden payload.
     * First determines if there is a message encoded by checking for the 32 bytes worth of 0's in the LSB's.
     * If there is a message it is decoded starting from the next byte using LSB steganography to determine each bit of each character until the sentinel value is reached. (a byte of 0's)
     */
    public static String decode(File carrier){
        int start = locatePixelArray(carrier);
        //open file
        try(FileInputStream stream = new FileInputStream(carrier)){
            //skip until pixel array starts
            stream.skip(start);

            //check if message has been encoded
            for(int i = 0; i < 32; i++){
                if((stream.read() & 1) != 0){
                    return "";
                }
            }

            //string together consecutive LSB's to obtain the payload
            String result = "";
            int character;
            while(true){
                character = 0;
                for(int i = 0; i < 8; i++){
                    character = character | ((stream.read() & 1) << i);
                }
                if(character == 0) break;
                result += (char) character;
            }

            //clean up and return
            return result;
        } catch(IOException e){
            if(true) //check exception type TODO
                return "Error: file not found";
            else
                return "Error: IO error";
        }
    }
    
    /**
     * Returns the space available for encoding steganographic messages in bytes.
     */
    public static int charactersAvailable(File carrier){
        return (int) (carrier.length() - locatePixelArray(carrier) + 32) / 8;
    }
}
