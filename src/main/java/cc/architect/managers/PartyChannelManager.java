package cc.architect.managers;

import cc.architect.Architect;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;

import static org.bukkit.Bukkit.getServer;

public class PartyChannelManager implements PluginMessageListener {
    public static final String PUBLIC_CHANNEL = "BungeeCord";
    public static final String PARTY_CHANNEL = "Architect:Party";
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        Bukkit.broadcastMessage("Received message from BungeeCord");
        if (!channel.equals(PUBLIC_CHANNEL)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        Bukkit.broadcastMessage("Received message from BungeeCord: " + subchannel);

        if(!subchannel.equals(PARTY_CHANNEL)) return;

        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);

        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
        try {
            String somedata = msgin.readUTF();
            Bukkit.broadcastMessage("Received message with data: " + somedata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void sendPartyChannelMessage(String subchannel, String data) {
        // Use the code sample in the 'Request' section below to send
        // the data.
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF(PARTY_CHANNEL);

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            msgout.writeUTF("Some kind of data here"); // You can do anything you want with msgout
        } catch (IOException exception){
            exception.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        getServer().sendPluginMessage(Architect.PLUGIN, PUBLIC_CHANNEL, out.toByteArray());
    }
}
