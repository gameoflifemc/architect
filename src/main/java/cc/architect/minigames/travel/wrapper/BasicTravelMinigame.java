package cc.architect.minigames.travel.wrapper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static cc.architect.minigames.travel.wrapper.TravelRegistry.entities;
import static org.bukkit.Bukkit.getPlayer;

public abstract class BasicTravelMinigame extends TravelMinigame{
    public int count;
    public int MAX_COUNT = 15;

    public BasicTravelMinigame(World finalWorld, int updateRate) {
        super(finalWorld, updateRate);
    }

    public abstract List<Location> entityLocations();
    public abstract List<ItemStack> getRequiredItems();
    public abstract List<ItemStack> getAllItems();

    public abstract Factory factory();

    @Override
    public void start(UUID player) {
        entityLocations().forEach(loc->{
            if(count>MAX_COUNT) return;

            factory().createSuper(loc);
            Entity e = factory().createSuper(loc);
            entities.put(e.getUniqueId(),e);
            count++;
        });
    }

    @Override
    public void updateCall() {
        if(count>MAX_COUNT) return;

        Location loc = entityLocations().get(new Random().nextInt(entityLocations().size()));

        if(loc.getNearbyEntities(5,5,5).stream().filter(e->!(e instanceof Player)).count()>3) return;

        Entity e = factory().createSuper(loc);
        entities.put(e.getUniqueId(),e);
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
        Entity e = event.getEntity();
        if(!e.getLocation().getWorld().getName().equals(travelWorld.getName())) return;

        entities.remove(e.getUniqueId());

        count = Math.max(0,count-1);
    }
}
