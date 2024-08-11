package cc.architect.channels;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static cc.architect.channels.BaseChannels.getBasicMessage;
import static cc.architect.channels.BaseChannels.sendToDefaultChannel;

public class PlayerLister implements PluginMessageListener {
    public static final List<Consumer<List<String>>> queue = new ArrayList<>();
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
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
