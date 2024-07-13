package cc.architect;

import java.io.DataInputStream;
import java.io.IOException;

public class Utilities {
    /**
     * Add negative spaces in between a string.
     * @param text Text to add negative spaces to.
     * @return Text with negative spaces.
     */
    public static String addNegativeSpaces(String text) {
        // prepare new string
        StringBuilder sb = new StringBuilder();
        // remove spaces
        for (int i = 0; i < text.length(); i++) {
            sb.append(text.charAt(i)).append("@");
        }
        return sb.toString();
    }
    public static String readUTF(DataInputStream dataStream) {
        try {
            return dataStream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
