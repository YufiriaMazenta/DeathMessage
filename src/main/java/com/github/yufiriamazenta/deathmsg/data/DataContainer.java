package com.github.yufiriamazenta.deathmsg.data;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataContainer {

    private static Map<String, List<String>> deathMessageMap;

    public static void reloadMap() {
        deathMessageMap = new HashMap<>();
        DeathMessage.plugin.reloadConfig();
        FileConfiguration pluginConfig = DeathMessage.plugin.getConfig();
        for (String key : pluginConfig.getKeys(false)) {
            deathMessageMap.put(key.replace("_", "."), pluginConfig.getStringList(key));
        }
    }

    public static Map<String, List<String>> getDeathMessageMap() {
        return deathMessageMap;
    }

    public static void setDeathMessageMap(Map<String, List<String>> deathMessageMap) {
        DataContainer.deathMessageMap = deathMessageMap;
    }

    public static boolean hasDeathCause(String deathCause) {
        return deathMessageMap.containsKey(deathCause);
    }

    public static String getMessage(String deathCause) {
        if (hasDeathCause(deathCause)) {
            List<String> list = deathMessageMap.get(deathCause);
            return list.get(DeathMessage.random.nextInt(list.size()));
        }
        return null;
    }
}
