package cc.architect.minigames.travel.farm.mine;

import cc.architect.Architect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Bogged;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

public class BoggedFactory {
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
    public static Entity createMinerZombie(Location loc) {
        Bogged bogged = loc.getWorld().spawn(loc, Bogged.class);

        bogged.setAggressive(true);
        bogged.setAware(true);
        bogged.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
        bogged.setRemoveWhenFarAway(false);
        bogged.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,1, false, false));

        EntityEquipment equipment = ((LivingEntity)bogged).getEquipment();
        equipment.setItemInMainHand(new ItemStack(Material.IRON_HOE,1));
        equipment.setItemInOffHand(FarmTravel.shard);

        equipment.setDropChance(EquipmentSlot.HAND,0);
        equipment.setDropChance(EquipmentSlot.OFF_HAND,0.25f);
        equipment.setDropChance(EquipmentSlot.HEAD,0);
        bogged.setLootTable(nullTable);

        return bogged;
    }

}
