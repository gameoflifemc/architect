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

import static cc.architect.channels.Base.getForwardMessageData;
import static cc.architect.channels.ServerName.getServerName;
import static org.bukkit.Bukkit.getServer;

public class PlayerFinder implements PluginMessageListener {
    public static final Map<String, Consumer<String>> playerServerQueue = new HashMap<>();
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if(!channel.equals(Base.PUBLIC)){
            return;
        }

        //decode message
        ByteArrayDataInput request = ByteStreams.newDataInput(message);
        String subChannel = request.readUTF();

        //checks if request is from BaseChannels.GET_PLAYER_SERVER_CHANNEL
        if(!subChannel.equals(Base.GET_PLAYER_SERVER)){
            return;
        }

        //gets message data
        DataInputStream messageData = getForwardMessageData(request);

        String type = Utilities.readUTF(messageData);
        String serverName = Utilities.readUTF(messageData);
        String playerName = Utilities.readUTF(messageData);

        //if message is a response, and player is waiting for response, accept server name
        if(!type.equals(Base.REQUEST)){
            playerServerQueue.get(playerName).accept(serverName);
            playerServerQueue.remove(playerName);
            return;
        }

        //if message is a request, and player is online, send server name
        if(getServer().getPlayer(playerName) != null
                && getServer().getPlayer(playerName).isOnline()){

            Base.prepareForwardMessage();
            DataOutputStream messageOut = Base.getForwardMessageData();

            try {
                messageOut.writeUTF(Base.RESPONSE);
                messageOut.writeUTF(getServerName());
                messageOut.writeUTF(playerName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Base.sendForwardMessage(serverName, Base.GET_PLAYER_SERVER);
        }

    }
    public static void getPlayerServer(String playerName, Consumer<String> consumer){

        Base.prepareForwardMessage();
        DataOutputStream messageOut = Base.getForwardMessageData();
        try {
            messageOut.writeUTF(Base.REQUEST);
            messageOut.writeUTF(getServerName());
            messageOut.writeUTF(playerName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        playerServerQueue.put(playerName, consumer);
        Base.sendForwardMessage("ALL", Base.GET_PLAYER_SERVER);
    }

}
