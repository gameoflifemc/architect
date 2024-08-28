package cc.architect.channels;

import cc.architect.Architect;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static cc.architect.channels.Base.getBasicMessage;
import static cc.architect.channels.Base.sendToDefaultChannel;

public class PlayerLister implements PluginMessageListener {
    public static final List<Consumer<List<String>>> queue = new ArrayList<>();
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if(!channel.equals(Base.PUBLIC)){
            return;
        }

        //decode message
        ByteArrayDataInput request = ByteStreams.newDataInput(message);
        String subChannel = request.readUTF();

        //checks if request is from BaseChannels.GET_PLAYER_SERVER_CHANNEL
        if(!subChannel.equals(Base.PLAYER_LIST)){
            return;
        }

        String server = request.readUTF();
        String[] playerList = request.readUTF().split(", ");
        
        Architect.CONSOLE.sendMessage("Receive: "+Arrays.toString(playerList));

        queue.getFirst().accept(new ArrayList<>(Arrays.asList(playerList)));
    }
    public static void getPlayerList(Consumer<List<String>> consumer){
        queue.add(consumer);

        Architect.CONSOLE.sendMessage("Requesting player list...");
        ByteArrayDataOutput out = getBasicMessage(Base.PLAYER_LIST);
        out.writeUTF("ALL");
        sendToDefaultChannel(out);
    }

}
