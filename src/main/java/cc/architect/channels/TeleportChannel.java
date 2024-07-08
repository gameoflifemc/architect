package cc.architect.channels;

import cc.architect.Utilities;
import cc.architect.events.player.Join;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;

import static cc.architect.channels.BaseChannels.*;

public class TeleportChannel implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals(PUBLIC_CHANNEL)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if(!subchannel.equals(TELEPORT_CHANNEL)) return;

        DataInputStream messageData = getForwardMessageData(in);
        String receiver = Utilities.readUTF(messageData);
        String sender = Utilities.readUTF(messageData);

        Join.pendingJoin.put(receiver, sender);
    }
}
