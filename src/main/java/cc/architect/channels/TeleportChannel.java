package cc.architect.channels;

import cc.architect.Utilities;
import cc.architect.events.player.Join;
import cc.architect.managers.PartyManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;

import static cc.architect.channels.BaseChannels.getForwardMessageData;

public class TeleportChannel implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(BaseChannels.PUBLIC)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (!subChannel.equals(BaseChannels.TELEPORT)) {
            return;
        }
        DataInputStream messageData = getForwardMessageData(in);
        String receiver = Utilities.readUTF(messageData);
        String sender = Utilities.readUTF(messageData);
        Join.pendingJoin.put(receiver, sender);
        PartyManager.setPartiesMap(receiver, sender);
    }
}
