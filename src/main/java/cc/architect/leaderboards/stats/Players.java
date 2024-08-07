package cc.architect.leaderboards.stats;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.matcher.NodeMatcher;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Players {
    public static List<UUID> getAllPlayers() {
        InheritanceNode node = InheritanceNode.builder(LuckPermsProvider.get().getGroupManager().getGroup("default")).build();
        NodeMatcher<InheritanceNode> matcher = NodeMatcher.key(node);
        try {
            return LuckPermsProvider.get().getUserManager().searchAll(matcher).get().keySet().stream().toList();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
