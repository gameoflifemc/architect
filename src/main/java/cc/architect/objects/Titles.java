package cc.architect.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;

public class Titles {
    private static final Duration TRANSITION_FADE = Duration.ofMillis(500); // 0.5 second
    private static final Duration TRANSITION_STAY = Duration.ofMillis(10000); // 10 seconds
    public static final int TRANSITION_TELEPORT = 50; // 2.5 seconds
    public static final Title TRANSITION1 = Title.title(Component.empty(),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(TRANSITION_FADE,TRANSITION_STAY,Duration.ZERO)
    );
    public static final Title TRANSITION2 = Title.title(Component.text("$#").font(Fonts.LOADING),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(Duration.ZERO,TRANSITION_STAY,Duration.ZERO)
    );
    public static final Title TRANSITION3 = Title.title(Component.empty(),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(Duration.ZERO,TRANSITION_FADE,TRANSITION_FADE)
    );
    public static Title ROUTINE(String day, String routine) {
        return Title.title(Component.text("Den " + day),Component.text(routine),
            Title.Times.times(Duration.ZERO,Duration.ofMillis(3000),Duration.ZERO)
        );
    }
    public static Title DAY(String day) {
        return Title.title(Component.text("Den " + (Integer.parseInt(day) - 1)),Component.text("Dokončen!"),
            Title.Times.times(Duration.ZERO,Duration.ofMillis(3000),Duration.ZERO)
        );
    }
}
