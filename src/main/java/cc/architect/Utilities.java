package cc.architect;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class Utilities {
    public static final Map<Character, Integer> charWidth = Map.of(
        'I',3,
        'f',4,
        'i',1,
        'k',4,
        'l',2,
        't',3,
        ' ',3,
        'í',2,
        'ť',3,
        '.',1
    );
    public static String readUTF(DataInputStream dataStream) {
        try {
            return dataStream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean rollRandom(float percentage) {
        Random random = new Random();
        float randFloat = random.nextFloat() * 100;
        return randFloat <= percentage;
    }
    public static Component getSpacedComponent(Component first, Component last, Component spacer, int length) {
        Component builder = Component.text("");
        int textLen = getPixelLength(
                    ((TextComponent)first).content(),
                    first.style().hasDecoration(TextDecoration.BOLD)
                )
                +getPixelLength(
                        ((TextComponent)last).content(),
                        last.style().hasDecoration(TextDecoration.BOLD)
                );
        int neededLen = length - textLen;

        builder = builder.append(first);

        for(int i = 0; i < neededLen; i += 2){
            builder = builder.append(spacer);
        }
        /*if(neededLen % 2 == 1){
            builder = builder.append(spacer);
        }*/

        builder = builder.append(Component.text(" "));

        builder = builder.append(last);

        return builder;
    }
    public static Component getDottedComponent(Component first, Component last, int length) {
        return getSpacedComponent(first, last, Component.text(".", TextColor.color(40,40,40)),length);
    }
    public static int getPixelLength(String s){
        return getPixelLength(s,false);
    }

    /**
     * returns the pixel length of a string
     * @param s text
     * @param isBold if the text is bold (it will add 1 to everyting)
     * @return
     */
    public static int getPixelLength(String s, boolean isBold){
        int currentLength = 0;
        for(char ch : s.toCharArray()){
            currentLength += charWidth.getOrDefault(ch,5)+(isBold?2:1);
        }
        return currentLength;
    }
}
