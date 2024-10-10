package cc.architect.tasks.player;

import cc.architect.objects.Colors;
import cc.architect.objects.Icons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Random;

public class Tips implements Runnable {
    Component PREFIX = Icons.PROTIP.append(Component.text("[PROTIP] ").color(Colors.PROTIP));
    ArrayList<Component> VILLAGE = new ArrayList<>(){{
        add(Component.text("Nasbírej skóre a vyhraj super ceny v naší soutěži. Zaregistrovat se můžeš kliknutím na tuto zprávu!").clickEvent(ClickEvent.openUrl("https://www.bridgeacademy.cz/cz/financni-gramotnost-deti#registrace")));
        add(Component.text("Do dolu si můžeš koupit lepší svítilnu - hledej stánek Anny Černé."));
        add(Component.text("Spoření můžeš vyzvednout již druhý den - může ti pomoc při nečekaném výdaji."));
        add(Component.text("Do dolů a farmy se můžeš dostat i po vodě - poohlídni se kolem doků."));
        add(Component.text("Blesky v tvém hotbaru jsou tvoje energie. Potom, co ji všechnu vypotřebuješ, se ti obnoví a začne další den.\n"));
        add(Component.text("Ve vodě můžeš najít volně položené Nautilus ulity. Jakub Jan v plážové části vesnice si je velice rád koupí."));
        add(Component.text("Už jsi vyřešil, jak opravit redstone dveře? Hledej úkol za školou.\n"));
        add(Component.text("Možná by mělo cenu si popovídat s Karmen Karmínovou v části vesnice v březovém lese."));
        add(Component.text("Můžeš pomoci Hostinskému, určitě se ti náležitě odmění."));
    }};
    ArrayList<Component> MINE = new ArrayList<>(){{
        add(Component.text("Nasbírej skóre a vyhraj super ceny v naší soutěži. Zaregistrovat se můžeš kliknutím na tuto zprávu!").clickEvent(ClickEvent.openUrl("https://www.bridgeacademy.cz/cz/financni-gramotnost-deti#registrace")));
        add(Component.text("Do dolu si můžeš koupit lepší svítilnu - hledej stánek Anny Černé."));
        add(Component.text("V dolech se nemusíš vstupovat pouze hlavním vchodem. Hledej i ty tajné, pokud chceš změnu."));
        add(Component.text("Když těžíš kámen, můžeš najít poklad."));
        add(Component.text("Pokud přineseš Zdendovi 64 kusů jedné rudy, dá ti unikátní část brnění. Tedy krom uhlí. To se ti náležitě peněžně odmění."));
        add(Component.text("Zkus prodat svou rudu na tržišti ve vesnici. Možná budou ceny trochu jiné."));
    }};
    ArrayList<Component> FARM = new ArrayList<>() {{
        add(Component.text("Nasbírej skóre a vyhraj super ceny v naší soutěži. Zaregistrovat se můžeš kliknutím na tuto zprávu!").clickEvent(ClickEvent.openUrl("https://www.bridgeacademy.cz/cz/financni-gramotnost-deti#registrace")));
        add(Component.text("Už jsi se díval, co roste v korunách stromů?"));
        add(Component.text("Pokud se nechceš pořád vracet pro hnojivo do mlýna, můžeš si ho vyměnit u kompostérů."));
        add(Component.text("Pokud ti dojde hnojivo, v mlýně dostaneš 32 zdarma. Ale pozor, pokaždé se ti smaže všechno hnojivo co máš v inventáři."));
        add(Component.text("Některé plodiny jdou kombinovat za lepší cenu, když je prodáš spolu."));
        add(Component.text("Zkus některé plodiny prodat na tržišti. Možná je na některé výhodnější nabídka."));
    }};
    @Override
    public void run() {
        Random random = new Random();
        Bukkit.getOnlinePlayers().forEach(p -> {
            switch (p.getWorld().getName()) {
                case "village":
                    p.sendMessage(PREFIX.append(VILLAGE.get(random.nextInt(VILLAGE.size())).color(Colors.PROTIP)));
                    return;
                case "mine":
                    p.sendMessage(PREFIX.append(MINE.get(random.nextInt(MINE.size())).color(Colors.PROTIP)));
                    return;
                case "farm":
                    p.sendMessage(PREFIX.append(FARM.get(random.nextInt(FARM.size())).color(Colors.PROTIP)));
            }
        });
    }
}
