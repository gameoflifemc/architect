package cc.architect.channels;

import cc.architect.Utilities;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static cc.architect.channels.BaseChannels.*;
import static cc.architect.channels.ServerName.getServerName;
import static org.bukkit.Bukkit.getServer;

public class PlayerLister implements PluginMessageListener {
    public static List<Consumer<List<String>>> queue = new ArrayList<>();
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if(!channel.equals(BaseChannels.PUBLIC)){
            return;
        }

        //decode message
        ByteArrayDataInput request = ByteStreams.newDataInput(message);
        String subChannel = request.readUTF();

        //checks if request is from BaseChannels.GET_PLAYER_SERVER_CHANNEL
        if(!subChannel.equals(BaseChannels.PLAYER_LIST)){
            return;
        }

        String server = request.readUTF();
        String[] playerList = request.readUTF().split(", ");

        Bukkit.getConsoleSender().sendMessage("Recieve: "+Arrays.toString(playerList));

        queue.getFirst().accept(new ArrayList<>(Arrays.asList(playerList)));
    }
    public static void getPlayerList(Consumer<List<String>> consumer){
        queue.add(consumer);

        Bukkit.getConsoleSender().sendMessage("Requesting player list...");
        ByteArrayDataOutput out = getBasicMessage(BaseChannels.PLAYER_LIST);
        out.writeUTF("ALL");
        sendToDefaultChannel(out);
    }

}
