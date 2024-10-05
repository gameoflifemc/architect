package cc.architect.channels;

import cc.architect.Architect;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import static cc.architect.channels.Base.*;

public class ProxyLogger {
    public static void logInfoProxy(String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(message);
        Base.sendToProxyLoggerChannel(out);
    }
}
