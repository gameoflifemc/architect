package cc.architect.minigames.travel.mine;

import cc.architect.Architect;
import cc.architect.minigames.travel.wrapper.Factory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ZombieFactory extends Factory {
    public static LootTable nullTable = new LootTable() {
        @Override
        public @NotNull Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext context) {
            return List.of();
        }

        @Override
        public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext context) {

        }

        @Override
        public @NotNull NamespacedKey getKey() {
            return new NamespacedKey(Architect.PLUGIN,"null");
        }
    };
    public void create(Location loc) {
        Zombie zombie = loc.getWorld().spawn(loc, Zombie.class);

        zombie.setAggressive(true);
        zombie.setAware(true);
        zombie.setAdult();
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
        zombie.setRemoveWhenFarAway(false);
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,1, false, false));
        zombie.setShouldBurnInDay(false);

        EntityEquipment equipment = ((LivingEntity)zombie).getEquipment();
        equipment.setItemInMainHand(new ItemStack(Material.IRON_PICKAXE,1));
        equipment.setItemInOffHand(MineTravel.shard);
        boolean isHelmet = Math.random() > 0.7;
        if(isHelmet) {
            equipment.setHelmet(new ItemStack(Material.LEATHER_HELMET,1));
        }

        equipment.setDropChance(EquipmentSlot.HAND,0);
        equipment.setDropChance(EquipmentSlot.OFF_HAND,0.25f);
        equipment.setDropChance(EquipmentSlot.HEAD,0);
        zombie.setLootTable(nullTable);
        zombie.addScoreboardTag("minerZ");
    }

    @Override
    public EntityType entityType() {
        return EntityType.ZOMBIE;
    }
}
