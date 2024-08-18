package cc.architect.objects;

import net.kyori.adventure.key.Key;

import java.util.List;

public class Fonts {
    public static final Key AVATAR = Key.key("minecraft:avatar");
    public static final Key ACTION = Key.key("minecraft:action");
    public static final Key LOADING = Key.key("minecraft:loading");
    public static final List<Key> COMPASS = List.of(
        Key.key("minecraft:compass0"),
        Key.key("minecraft:compass1"),
        Key.key("minecraft:compass2"),
        Key.key("minecraft:compass3"),
        Key.key("minecraft:compass4"),
        Key.key("minecraft:compass5"),
        Key.key("minecraft:compass4"),
        Key.key("minecraft:compass3"),
        Key.key("minecraft:compass2"),
        Key.key("minecraft:compass1"),
        Key.key("minecraft:compass0")
    );
}
