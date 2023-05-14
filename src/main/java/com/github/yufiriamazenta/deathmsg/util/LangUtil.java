package com.github.yufiriamazenta.deathmsg.util;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class LangUtil {

    private static final Pattern colorPattern = Pattern.compile("&#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");

    public static String color(String text) {
        StringBuilder strBuilder = new StringBuilder(text);
        Matcher matcher = colorPattern.matcher(strBuilder);
        while (matcher.find()) {
            String colorCode = matcher.group();
            String colorStr = ChatColor.of(colorCode.substring(1)).toString();
            strBuilder.replace(matcher.start(), matcher.start() + colorCode.length(), colorStr);
            matcher = colorPattern.matcher(strBuilder);
        }
        text = strBuilder.toString();
        return translateAlternateColorCodes('&', text);
    }

    public static void msg(CommandSender sender, String msgKey) {
        msg(sender, msgKey, new HashMap<>());
    }

    public static void msg(CommandSender sender, String msgKey, Map<String, String> formatMap) {
        if (sender == null) {
            return;
        }
        String message = DeathMessage.plugin.getConfig().getString(msgKey, msgKey);
        for (String formatStr : formatMap.keySet()) {
            message = message.replace(formatStr, formatMap.get(formatStr));
        }
        sender.sendMessage(color(message));
    }

}
