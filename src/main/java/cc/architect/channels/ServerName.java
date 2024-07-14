package cc.architect.channels;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import static cc.architect.channels.BaseChannels.getBasicMessage;
import static cc.architect.channels.BaseChannels.sendToDefaultChannel;

public class ServerName implements PluginMessageListener {
    public static String name = null;
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if(!channel.equals(BaseChannels.PUBLIC)){
            return;
        }

        //decode message
        ByteArrayDataInput request = ByteStreams.newDataInput(message);
        String subChannel = request.readUTF();

        //checks if request is from GET_PLAYER_SERVER_CHANNEL
        if(!subChannel.equals(BaseChannels.GET_SERVER)){
            return;
        }

        name = request.readUTF();
        Bukkit.getConsoleSender().sendMessage("This server name is " + name);
    }

    public static void requestServerName(){
        Bukkit.getConsoleSender().sendMessage("Requesting name...");
        ByteArrayDataOutput out = getBasicMessage(BaseChannels.GET_SERVER);
        sendToDefaultChannel(out);
    }

    public static String getServerName(){
        if(name==null){
            requestServerName();
        }
        return name;
    }
}
