package cc.architect.minigames.travel.wrapper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public abstract class BasicTravelMinigame extends TravelMinigame{
    public int count;
    public static int MAX_COUNT = 15;

    public BasicTravelMinigame(World finalWorld, int updateRate) {
        super(finalWorld, updateRate);
    }

    public abstract List<Location> entityLocations();

    public abstract List<ItemStack> onSpawnItems();
    public abstract List<ItemStack> getRequiredItems();
    public abstract List<ItemStack> getAllItems();

    public abstract Factory factory();

    @Override
    public void start(UUID player) {
        for(ItemStack allItems : onSpawnItems()) {
            getPlayer(player).getInventory().addItem(allItems);
        }
        entityLocations().forEach(loc->{
            if(count>MAX_COUNT) return;

            factory().create(loc);
            count++;
        });
    }

    @Override
    public void updateCall() {
        if(count>MAX_COUNT) return;

        Location loc = entityLocations().get(new Random().nextInt(entityLocations().size()));

        if(loc.getNearbyEntities(5,5,5).stream().filter(e->!(e instanceof Player)).count()>3) return;

        factory().create(loc);
        count++;
    }

    @Override
    public boolean canExit(UUID player) {
        boolean out = true;

        for(ItemStack required : getRequiredItems()) {
            out = out && getPlayer(player).getInventory().contains(required.getType());
        }

        if(out) {
            for(ItemStack allItems : getAllItems()) {
                getPlayer(player).getInventory().remove(allItems.getType());
            }
        }
        return out;
    }

    public void entityDeath(EntityDeathEvent event) {
        if(!event.getEntityType().equals(factory().entityType())) return;
        if(!event.getEntity().getLocation().getWorld().getName().equals(travelWorld.getName())) return;

        count = Math.max(0,count-1);
    }
}
