package cc.architect.channels;

import cc.architect.Architect;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class BaseChannels {
    public static final String PUBLIC_CHANNEL = "BungeeCord";
    public static final String PARTY_CHANNEL = "Architect:Party";
    public static final String INVITE_CHANNEL = "Architect:Invite";
    public static final String FORWARD_CHANNEL = "Forward";
    public static final String GET_SERVER_NAME_CHANNEL = "GetServer";
    public static final String GET_PLAYER_SERVER_CHANNEL = "Architect:GetPlayerServer";
    public static final String REQUEST = "Request";
    public static final String RESPONSE = "Response";

    public static ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
    public static DataOutputStream msgout = new DataOutputStream(msgbytes);
    public static void sendToDefaultChannel(ByteArrayDataOutput out){
        Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(Architect.PLUGIN, PUBLIC_CHANNEL, out.toByteArray());
    }
    public static void sendToDefaultChannelPlayer(ByteArrayDataOutput out, Player player){
        player.sendPluginMessage(Architect.PLUGIN, PUBLIC_CHANNEL, out.toByteArray());
    }

    public static ByteArrayDataOutput getBasicMessage(String subchannel){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        return out;
    }
    public static void prepareForwardMessage(){
        msgbytes = new ByteArrayOutputStream();
        msgout = new DataOutputStream(msgbytes);
    }
    public static DataOutputStream getForwardMessageData(){
        return msgout;
    }
    public static void sendForwardMessage(String server, String channel){
        ByteArrayDataOutput out = getBasicMessage(FORWARD_CHANNEL);
        out.writeUTF(server);
        out.writeUTF(channel);

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
        sendToDefaultChannel(out);
    }
    public static void sendForwardMessage(String server, String channel, Player player){
        ByteArrayDataOutput out = getBasicMessage(FORWARD_CHANNEL);
        out.writeUTF(server);
        out.writeUTF(channel);

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
        sendToDefaultChannelPlayer(out,player);
    }
    public static DataInputStream getForwardMessageData(ByteArrayDataInput in){
        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);

        return new DataInputStream(new ByteArrayInputStream(msgbytes));
    }
}
