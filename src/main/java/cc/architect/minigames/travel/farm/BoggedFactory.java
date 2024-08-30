package cc.architect.minigames.travel.farm;

import cc.architect.minigames.travel.mine.ZombieFactory;
import cc.architect.minigames.travel.wrapper.Factory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Bogged;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BoggedFactory extends Factory {
    public Entity create(Location loc) {
        Bogged bogged = loc.getWorld().spawn(loc, Bogged.class);

        bogged.setAggressive(true);
        bogged.setAware(true);
        bogged.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
        bogged.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(.3f);
        bogged.getAttribute(Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY).setBaseValue(3f);
        bogged.setRemoveWhenFarAway(false);
        bogged.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,1, false, false));
        bogged.setShouldBurnInDay(false);

        EntityEquipment equipment = ((LivingEntity)bogged).getEquipment();
        equipment.setItemInMainHand(new ItemStack(Material.IRON_HOE,1));
        equipment.setItemInOffHand(FarmTravel.key);

        equipment.setDropChance(EquipmentSlot.HAND,0);
        equipment.setDropChance(EquipmentSlot.OFF_HAND,0.25f);
        equipment.setDropChance(EquipmentSlot.HEAD,0);
        bogged.setLootTable(ZombieFactory.nullTable);
        bogged.addScoreboardTag("farmB");

        return bogged;
    }

    @Override
    public EntityType entityType() {
        return EntityType.BOGGED;
    }

}
