package cc.architect.leaderboards.stats;

import cc.architect.Architect;
import net.luckperms.api.node.matcher.NodeMatcher;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Players {
    public static List<UUID> getAllPlayers() {
        InheritanceNode node = InheritanceNode.builder(Architect.LUCKPERMS.getGroupManager().getGroup("default")).build();
        NodeMatcher<InheritanceNode> matcher = NodeMatcher.key(node);
        try {
            return Architect.LUCKPERMS.getUserManager().searchAll(matcher).get().keySet().stream().toList();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
