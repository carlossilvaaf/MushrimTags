package me.mushrim.tags;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Set;

public class TagCommand implements CommandExecutor {

    private final TagManager tagManager;

    public TagCommand(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Apenas jogadores podem usar esse comando.");
            return true;
        }

        Player player = (Player) sender;

        // /tag
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Tags disponíveis:");

            Set<String> tags = tagManager.getAvailableTags();
            if (tags.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Nenhuma tag disponível.");
                return true;
            }

            TextComponent allTags = new TextComponent("");

            int count = 0;
            for (String tagName : tags) {
                String prefix = tagManager.getPrefix(tagName);
                String color = ChatColor.getLastColors(prefix);
                TextComponent tagComponent = new TextComponent(color + tagName);
                tagComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tagName));
                tagComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Clique para usar a tag \"" + color + tagName + ChatColor.RESET + "\"").create()));

                allTags.addExtra(tagComponent);

                count++;
                if (count < tags.size()) {
                    allTags.addExtra(new TextComponent(ChatColor.GRAY + ", "));
                }
            }

            player.spigot().sendMessage(allTags);
            return true;
        }

        String tagName = args[0].toLowerCase();

        if (!tagManager.hasTag(tagName)) {
            player.sendMessage(ChatColor.RED + "Essa tag não existe.");
            return true;
        }

        tagManager.setPlayerTag(player.getUniqueId(), tagName);
        tagManager.applyTag(player);

        player.sendMessage(ChatColor.GREEN + "Sua tag foi definida para: " + tagManager.getPrefix(tagName) + ChatColor.RESET + player.getName());
        return true;
    }
}
