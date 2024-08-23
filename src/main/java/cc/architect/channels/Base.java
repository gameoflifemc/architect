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

public class Base {
    public static final String PUBLIC = "BungeeCord";
    public static final String CONNECT = "Connect";
    public static final String PLAYER_LIST = "PlayerList";
    public static final String INVITE = "Architect:Invite";
    public static final String TELEPORT = "Architect:Teleport";
    public static final String GET_PLAYER_SERVER = "Architect:GetPlayerServer";
    public static final String FORWARD = "Forward";
    public static final String GET_SERVER = "GetServer";
    public static final String REQUEST = "Request";
    public static final String RESPONSE = "Response";
    public static final String LIMBO = "mobilave";
    public static ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
    public static DataOutputStream msgOut = new DataOutputStream(msgBytes);
    public static void sendToDefaultChannel(ByteArrayDataOutput out){
        Iterables.getFirst(Bukkit.getOnlinePlayers(),null).sendPluginMessage(Architect.PLUGIN, Base.PUBLIC,out.toByteArray());
    }
    public static void sendToDefaultChannelPlayer(ByteArrayDataOutput out,Player player){
        player.sendPluginMessage(Architect.PLUGIN, Base.PUBLIC,out.toByteArray());
    }
    public static ByteArrayDataOutput getBasicMessage(String subChannel){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subChannel);
        return out;
    }
    public static void prepareForwardMessage(){
        msgBytes = new ByteArrayOutputStream();
        msgOut = new DataOutputStream(msgBytes);
    }
    public static DataOutputStream getForwardMessageData() {
        return msgOut;
    }
    public static void sendForwardMessage(String server, String channel){
        ByteArrayDataOutput out = getBasicMessage(Base.FORWARD);
        out.writeUTF(server);
        out.writeUTF(channel);
        out.writeShort(msgBytes.toByteArray().length);
        out.write(msgBytes.toByteArray());
        sendToDefaultChannel(out);
    }
    public static void sendForwardMessage(String server, String channel, Player player){
        ByteArrayDataOutput out = getBasicMessage(Base.FORWARD);
        out.writeUTF(server);
        out.writeUTF(channel);
        out.writeShort(msgBytes.toByteArray().length);
        out.write(msgBytes.toByteArray());
        sendToDefaultChannelPlayer(out,player);
    }
    public static DataInputStream getForwardMessageData(ByteArrayDataInput in){
        short input = in.readShort();
        byte[] msgBytes = new byte[input];
        in.readFully(msgBytes);
        return new DataInputStream(new ByteArrayInputStream(msgBytes));
    }
}
