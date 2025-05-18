package me.mushrim.tags;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class TagListener implements Listener {

    private final TagManager tagManager;

    public TagListener(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        tagManager.applyTag(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefix = tagManager.getPlayerPrefix(player.getUniqueId());

        event.setFormat(prefix + player.getName() + ": " + event.getMessage());
    }
}
