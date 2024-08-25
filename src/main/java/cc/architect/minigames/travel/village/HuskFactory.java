package cc.architect.minigames.travel.village;

import cc.architect.Architect;
import cc.architect.minigames.travel.wrapper.Factory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
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

public class HuskFactory extends Factory {
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
        Zombie husk = loc.getWorld().spawn(loc, Husk.class);

        husk.setAggressive(true);
        husk.setAware(true);
        husk.setAdult();
        husk.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
        husk.setRemoveWhenFarAway(false);
        husk.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,1, false, false));
        husk.setShouldBurnInDay(false);

        EntityEquipment equipment = ((LivingEntity)husk).getEquipment();
        equipment.setItemInMainHand(new ItemStack(Material.WOODEN_SWORD,1));
        equipment.setItemInOffHand(VillageTravel.stick);
        boolean isHelmet = Math.random() > 0.7;
        if(isHelmet) {
            equipment.setHelmet(new ItemStack(Material.LEATHER_HELMET,1));
        }

        equipment.setDropChance(EquipmentSlot.HAND,0);
        equipment.setDropChance(EquipmentSlot.OFF_HAND,0.25f);
        equipment.setDropChance(EquipmentSlot.HEAD,0);
        husk.setLootTable(nullTable);
        husk.addScoreboardTag("villageH");
    }

    @Override
    public EntityType entityType() {
        return EntityType.ZOMBIE;
    }
}
