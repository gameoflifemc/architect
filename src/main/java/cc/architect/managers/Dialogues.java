package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.Utilities;
import cc.architect.objects.DialoguePosition;
import cc.architect.objects.HashMaps;
import cc.architect.objects.Messages;
import cc.architect.objects.ResponseList;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Dialogues {
    /**
     * Insert a player into a dialogue
     * @param p Player
     * @param target Target location
     * @param uid Dialogue unique identifier
     */
    public static void enter(Player p, Location target, String uid) {
        // create a new dialogue position
        DialoguePosition pos = new DialoguePosition(p.getLocation(), target, uid);
        // add player to dialogue
        HashMaps.DIALOGUE_POSITIONS.put(p, pos);
        // disable the turn-to-player function of the NPC
        Npc npc = FancyNpcsPlugin.get().getNpcManager().getNpc(uid);
        npc.getData().setTurnToPlayer(false);
        // force player to look at target
        Dialogues.createCamera(p, pos);
        // give player overlay
        Utilities.giveOverlay(p);
        // send controls
        p.sendActionBar(Messages.ACTIONBAR_DIALOGUE_STANDARD);
        // handle the first stage
        Dialogues.start(p, uid);
        
        
        
        
        
        
        
        
        // prepare response list
        ResponseList responseList = new ResponseList(uid);
        // add response list to response lists
        HashMaps.RESPONSE_LISTS.put(p, responseList);
        // create a new objective
        Scoreboards.create(p);
        // show player responses
        Scoreboards.show(p);
        // send controls
        p.sendActionBar(Messages.ACTIONBAR_DIALOGUE_RESPONSE);
    }
    /**
     * Remove a player from a dialogue
     * @param p Player
     */
    public static void leave(Player p) {
        // get player's dialogue position
        DialoguePosition pos = HashMaps.DIALOGUE_POSITIONS.get(p);
        // re-enable the turn-to-player function of the NPC
        Npc npc = FancyNpcsPlugin.get().getNpcManager().getNpc(pos.getUid());
        npc.getData().setTurnToPlayer(true);
        // only do this if the player is online
        if (p.isOnline()) {
            // remove player from camera
            p.setSpectatorTarget(p);
            // teleport player back to original location
            p.teleport(pos.getLocation());
            // put player back in adventure mode
            p.setGameMode(GameMode.ADVENTURE);
            // remove overlay
            p.getInventory().setHelmet(ItemStack.empty());
            // send empty action bar
            p.sendActionBar(Component.empty());
        }
        // remove camera entity
        pos.getCamera().remove();
        // remove objective
        Scoreboards.remove(p);
        // remove player from dialogue
        HashMaps.DIALOGUE_POSITIONS.remove(p);
    }
    /**
     * Create a camera entity for a player
     * @param p Player
     */
    private static void createCamera(Player p, DialoguePosition pos) {
        // entity location
        Location loc = p.getEyeLocation();
        // summon camera entity
        ItemDisplay camera = loc.getWorld().spawn(loc,ItemDisplay.class);
        // set camera's teleport duration
        camera.setTeleportDuration(20);
        // put player in spectator mode
        p.setGameMode(GameMode.SPECTATOR);
        // set camera entity as the player's camera
        p.setSpectatorTarget(camera);
        // save camera entity to dialogue position
        pos.setCamera(camera);
        // new camera location
        Location newLoc = loc.clone();
        // set target rotations
        newLoc.setYaw(pos.getYaw());
        newLoc.setPitch(pos.getPitch());
        // teleport camera entity to target location
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN, () -> {
            // teleport camera entity
            camera.teleport(newLoc);
        },2);
    }
    private static void start(Player p, String uid) {
        Residents.CONFIGS.get(uid).getString("start." + uid + ".response1");
    }
}
