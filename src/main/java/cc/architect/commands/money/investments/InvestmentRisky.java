package cc.architect.commands.money.investments;

import cc.architect.managers.Game;
import cc.architect.managers.Meta;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public class InvestmentRisky {
    public static int investPut(CommandContext ctx) {
        return InvestmentGlobal.investPut(ctx, Meta.INVESTMENTS_MAP_RISKY);
    }

    public static int investClaim(CommandContext ctx) {
        return InvestmentGlobal.investClaim(ctx, Meta.INVESTMENTS_MAP_RISKY);
    }

    public static int investInspect(CommandContext ctx) {
        return InvestmentGlobal.investInspect(ctx, Meta.INVESTMENTS_MAP_RISKY);
    }

    public static void handleRiskyInvestmentsAdder(Player p) {
        StringBuilder invBuilder = new StringBuilder();
        String investmentsMap = Meta.get(p,Meta.INVESTMENTS_MAP_RISKY);
        if(investmentsMap == null) {
            Meta.set(p,Meta.INVESTMENTS_MAP_RISKY,"");
            return;
        }
        String[] investments = investmentsMap.split(";");
        if(investments[0].isEmpty()) return;

        int playerDay = Integer.parseInt(Meta.get(p,Meta.DAYS));

        for (String investment : investments) {
            String[] data = investment.split(",");
            int amount = Integer.parseInt(data[0]);
            int days = Math.max(Integer.parseInt(data[1])-playerDay,0);

            float randFloat = (float) ((Math.random() * (Game.INVESTMENTS_RISKY_MAX_PERCENT - Game.INVESTMENTS_RISKY_MIN_PERCENT)) + Game.INVESTMENTS_RISKY_MIN_PERCENT);
            randFloat = Math.round(randFloat*100)/100f;

            int newAmount = amount + (int)((double)amount * randFloat); //nechat castování na double i int

            if(days==0){
                newAmount = amount;
            }
            invBuilder.append(newAmount).append(",").append(days).append(";");
        }

        Meta.set(p,Meta.INVESTMENTS_MAP_RISKY,invBuilder.toString());
    }
}
