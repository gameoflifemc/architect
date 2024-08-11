package cc.architect;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;

import static java.lang.Math.cos;

public class Utilities {
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
        return (-(cos(Math.PI * (x / 100.0)) + 1.0) / 2.0) * 100.0;
    }
    public static boolean rollRandom(float percentage) {
        Random random = new Random();
        float randFloat = random.nextFloat() * 100;
        return randFloat <= percentage;
    }
}
