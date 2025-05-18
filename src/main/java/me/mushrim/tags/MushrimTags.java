package me.mushrim.tags;

import org.bukkit.plugin.java.JavaPlugin;

public class MushrimTags extends JavaPlugin {

    private static MushrimTags instance;
    private TagManager tagManager;

    @Override
    public void onEnable() {
        instance = this;

        saveResource("tags.yml", false);

        tagManager = new TagManager();
        getServer().getPluginManager().registerEvents(new TagListener(tagManager), this);
        getCommand("tag").setExecutor(new TagCommand(tagManager));
    }

    public static MushrimTags getInstance() {
        return instance;
    }
}
