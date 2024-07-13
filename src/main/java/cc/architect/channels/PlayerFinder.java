package cc.architect.channels;

import cc.architect.Utilities;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static cc.architect.channels.BaseChannels.*;
import static cc.architect.channels.ServerName.getServerName;
import static org.bukkit.Bukkit.getServer;

public class PlayerFinder implements PluginMessageListener {
    public static Map<String, Consumer<String>> playerServerQueue = new HashMap<>();
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if(!channel.equals(PUBLIC_CHANNEL)){
            return;
        }

        //decode message
        ByteArrayDataInput request = ByteStreams.newDataInput(message);
        String subChannel = request.readUTF();

        //checks if request is from GET_PLAYER_SERVER_CHANNEL
        if(!subChannel.equals(GET_PLAYER_SERVER_CHANNEL)){
            return;
        }

        //gets message data
        DataInputStream messageData = getForwardMessageData(request);

        String type = Utilities.readUTF(messageData);
        String serverName = Utilities.readUTF(messageData);
        String playerName = Utilities.readUTF(messageData);

        //if message is a response, and player is waiting for response, accept server name
        if(!type.equals(REQUEST)){
            playerServerQueue.get(playerName).accept(serverName);
            playerServerQueue.remove(playerName);
            return;
        }

        //if message is a request, and player is online, send server name
        if(getServer().getPlayer(playerName) != null
                && getServer().getPlayer(playerName).isOnline()){

            BaseChannels.prepareForwardMessage();
            DataOutputStream messageOut = BaseChannels.getForwardMessageData();

            try {
                messageOut.writeUTF(RESPONSE);
                messageOut.writeUTF(getServerName());
                messageOut.writeUTF(playerName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BaseChannels.sendForwardMessage(serverName,GET_PLAYER_SERVER_CHANNEL);
        }

    }
    public static void getPlayerServer(String playerName, Consumer<String> consumer){

        BaseChannels.prepareForwardMessage();
        DataOutputStream messageOut = BaseChannels.getForwardMessageData();
        try {
            messageOut.writeUTF(REQUEST);
            messageOut.writeUTF(getServerName());
            messageOut.writeUTF(playerName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        playerServerQueue.put(playerName, consumer);
        BaseChannels.sendForwardMessage("ALL",GET_PLAYER_SERVER_CHANNEL);
    }

}
