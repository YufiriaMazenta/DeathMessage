package com.github.yufiriamazenta.deathmsg.util;

import com.github.yufiriamazenta.deathmsg.DeathMessage;
import crypticlib.CrypticLib;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NmsUtil {

    private static Class<?> craftItemStackClass;
    private static Method asNmsCopyMethod;
    private static Method getTagCloneMethod;

    static {
        try {
            craftItemStackClass = Class.forName("org.bukkit.craftbukkit." + CrypticLib.nmsVersion() + ".inventory.CraftItemStack");
            asNmsCopyMethod = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            getTagCloneMethod = net.minecraft.world.item.ItemStack.class.getDeclaredMethod("getTagClone");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static String getItemTag(ItemStack itemStack) {
        String tag = "{}";
        try {
            net.minecraft.world.item.ItemStack nmsItem = (net.minecraft.world.item.ItemStack) asNmsCopyMethod.invoke(null, itemStack);
            getTagCloneMethod.setAccessible(true);
            tag = getTagCloneMethod.invoke(nmsItem).toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tag;
    }


}
