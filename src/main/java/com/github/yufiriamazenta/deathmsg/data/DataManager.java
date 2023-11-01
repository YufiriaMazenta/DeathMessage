package com.github.yufiriamazenta.deathmsg.data;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import crypticlib.util.MsgUtil;
import crypticlib.util.YamlConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {
    
    private final static Map<String, List<?>> deathMsgMap;
    public static Random random;

    static {
        deathMsgMap = new ConcurrentHashMap<>();
        random = new Random();
    }

    public static void reloadMap() {
        deathMsgMap.clear();
        DeathMessage.INSTANCE.reloadConfig();
        FileConfiguration config = DeathMessage.INSTANCE.getConfig();
        for (String key : config.getConfigurationSection("death_message").getKeys(false)) {
            deathMsgMap.put(key.replace("_", "."), YamlConfigUtil.configList2List(config.getList("death_message." + key, new ArrayList<>())));
        }

    }

    public static boolean hasDeathCause(String deathCause) {
        return deathMsgMap.containsKey(deathCause);
    }

    public static String getMessage(Player player, String deathCause) {
        if (hasDeathCause(deathCause)) {
//            System.out.println(deathMsgMap);
            List<?> list = deathMsgMap.get(deathCause);
            if (list == null || list.isEmpty())
                return null;
            if (list.get(0) instanceof Map) {
                List<String> messageList = new ArrayList<>();
                for (Object obj : list) {
                    Map<String, Object> map = (Map<String, Object>) obj;
                    if (!map.containsKey("perm") || player.hasPermission((String) map.get("perm"))) {
                        messageList = (List<String>) map.get("message");
                        break;
                    }
                }
                if (messageList.isEmpty())
                    return null;
                return messageList.get(random.nextInt(messageList.size()));
            } else if (list.get(0) instanceof String) {
                return (String) list.get(random.nextInt(list.size()));
            }
        }
        return null;
    }
}
