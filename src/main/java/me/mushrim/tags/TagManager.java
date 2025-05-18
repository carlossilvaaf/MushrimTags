package me.mushrim.tags;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TagManager {

    private final File tagFile;
    private final YamlConfiguration tagConfig;

    private final File dataFile;
    private JSONObject playerData;

    public TagManager() {
        tagFile = new File(MushrimTags.getInstance().getDataFolder(), "tags.yml");
        tagConfig = YamlConfiguration.loadConfiguration(tagFile);

        dataFile = new File(MushrimTags.getInstance().getDataFolder(), "playerdata.json");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                playerData = new JSONObject();
                saveData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loadData();
        }
    }


    public Set<String> getAvailableTags() {
        if (tagConfig.getConfigurationSection("tags") == null) return Collections.emptySet();
        return tagConfig.getConfigurationSection("tags").getKeys(false);
    }


    private void loadData() {
        try {
            JSONParser parser = new JSONParser();
            playerData = (JSONObject) parser.parse(new java.io.FileReader(dataFile));
        } catch (Exception e) {
            e.printStackTrace();
            playerData = new JSONObject();
        }
    }

    private void saveData() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            writer.write(playerData.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasTag(String name) {
        return tagConfig.getConfigurationSection("tags") != null &&
                tagConfig.getConfigurationSection("tags").contains(name);
    }

    public String getPrefix(String name) {
        return ChatColor.translateAlternateColorCodes('&', tagConfig.getString("tags." + name + ".prefix", ""));
    }

    public void setPlayerTag(UUID uuid, String tag) {
        playerData.put(uuid.toString(), tag);
        saveData();
    }

    public String getPlayerTag(UUID uuid) {
        Object tag = playerData.get(uuid.toString());
        return tag == null ? "" : tag.toString();
    }

    public String getPlayerPrefix(UUID uuid) {
        String tag = getPlayerTag(uuid);
        return tag.isEmpty() ? "" : getPrefix(tag);
    }

    public void applyTag(Player player) {
        String prefix = getPlayerPrefix(player.getUniqueId());
        player.setDisplayName(prefix + player.getName());
        player.setPlayerListName(prefix + player.getName());
    }
}
