package cc.architect;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;

import static java.lang.Math.cos;

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

    /**
     * Smoothing function for the cosine interpolation.
     * @param x Value to smooth. 0 - 100
     * @return Smoothed value. 0 - 100
     */
    public static double smoothingFunction(double x) {
        return (-(cos(Math.PI * (x/100.0)) + 1.0) / 2.0)*100.0;
    }

    //function for rare drops
    public static boolean chance(float percentage) {
        Random random = new Random();
        float randFloat = random.nextFloat() * 100;
        if (randFloat <= percentage) {
            return true;
        }

        return false;
    }
}
