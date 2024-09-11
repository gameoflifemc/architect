package cc.architect.commands.money.investments;

import cc.architect.managers.Meta;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

import static cc.architect.managers.Game.INVESTMENTS_PERCENT;

public class InvestmentBasic {
    public static int investPut(CommandContext ctx) {
        return InvestmentGlobal.investPut(ctx, Meta.INVESTMENTS_MAP);
    }

    public static int investClaim(CommandContext ctx) {
        return InvestmentGlobal.investClaim(ctx, Meta.INVESTMENTS_MAP);
    }

    public static int investInspect(CommandContext ctx) {
        return InvestmentGlobal.investInspect(ctx, Meta.INVESTMENTS_MAP);
    }
    public static void handleInvestmentsAdder(Player p) {
        StringBuilder invBuilder = new StringBuilder();
        String investmentsMap = Meta.get(p,Meta.INVESTMENTS_MAP);
        if(investmentsMap == null) {
            Meta.set(p,Meta.INVESTMENTS_MAP,"");
            return;
        }
        String[] investments = investmentsMap.split(";");
        if(investments[0].isEmpty()) return;

        int playerDay = Integer.parseInt(Meta.get(p,Meta.DAYS));

        for (String investment : investments) {
            String[] data = investment.split(",");
            int amount = Integer.parseInt(data[0]);
            int days = Math.max(Integer.parseInt(data[1])-playerDay,0);

            int newAmount = amount + (int)(amount * INVESTMENTS_PERCENT);
            if(days==0){
                newAmount = amount;
            }
            invBuilder.append(newAmount).append(",").append(days).append(";");
        }

        Meta.set(p,Meta.INVESTMENTS_MAP,invBuilder.toString());
    }
}
