package cc.architect;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;

import static java.lang.Math.cos;

public class Utilities {
    /**
     * Add negative spaces in between a string.
     * @param text Text to add negative spaces to.
     * @return Text with negative spaces.
     */
    public static String addNegativeSpaces(String text) {
        // prepare new string
        StringBuilder sb = new StringBuilder();
        // remove spaces
        for (int i = 0; i < text.length(); i++) {
            sb.append(text.charAt(i)).append("@");
        }
        return sb.toString();
    }
    public static String readUTF(DataInputStream dataStream) {
        try {
            return dataStream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Smoothing function for the cosine interpolation.
     * @param x Value to smooth. 0 - 100
     * @return Smoothed value. 0 - 100
     */
    public static double smoothingFunction(double x) {
        return (-(cos(Math.PI * (x / 100.0)) + 1.0) / 2.0) * 100.0;
    }
    //function for rare drops
    public static boolean rollRandom(float percentage) {
        Random random = new Random();
        float randFloat = random.nextFloat() * 100;
        return randFloat <= percentage;
    }
    public void addMetaData(Player player, String key, int value) {
        LuckPerms lp = LuckPermsProvider.get();
        
        // obtain CachedMetaData - the easiest way is via the PlayerAdapter
        // of course, you can get it via a User too if the player is offline.
        CachedMetaData metaData = lp.getPlayerAdapter(Player.class).getMetaData(player);
        
        // query & parse the meta value
        int current = metaData.getMetaValue(key,Integer::parseInt).orElse(0);
        
        // obtain a User instance (by any means! see above for other ways)
        User user = lp.getPlayerAdapter(Player.class).getUser(player);
        
        // create a new MetaNode holding the level value
        // of course, this can have context/expiry/etc too!
        MetaNode node = MetaNode.builder(key, Integer.toString(current + value)).build();
        
        // clear any existing meta nodes with the same key - we want to override
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals(key)));
        // add the new node
        user.data().add(node);
        
        // save!
        lp.getUserManager().saveUser(user);
    }
    public static void addMetaValue(User user, String key, int value) {
        int val = user.getCachedData().getMetaData().getMetaValue(key, Integer::parseInt).orElse(0);
        setMeta(user, key, Integer.toString(val + value));
    }

    public static void setMeta(User user, String key, String value) {
        MetaNode node = MetaNode.builder(key, value).build();
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals(key)));
        user.data().add(node);
        LuckPermsProvider.get().getUserManager().saveUser(user);
    }
}
