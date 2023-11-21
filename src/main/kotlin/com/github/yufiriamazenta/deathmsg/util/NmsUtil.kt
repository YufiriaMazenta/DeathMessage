package com.github.yufiriamazenta.deathmsg.util

import crypticlib.CrypticLib
import org.bukkit.inventory.ItemStack
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

object NmsUtil {
    private var craftItemStackClass: Class<*>? = null
    private var asNmsCopyMethod: Method? = null
    private var getTagCloneMethod: Method? = null

    init {
        try {
            craftItemStackClass =
                Class.forName("org.bukkit.craftbukkit." + CrypticLib.nmsVersion() + ".inventory.CraftItemStack")
            asNmsCopyMethod = craftItemStackClass!!.getMethod("asNMSCopy", ItemStack::class.java)
            getTagCloneMethod = net.minecraft.world.item.ItemStack::class.java.getDeclaredMethod("getTagClone")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
    }

    fun getItemTagJson(itemStack: ItemStack?): String {
        var tag = "{}"
        try {
            val nmsItem = asNmsCopyMethod!!.invoke(null, itemStack) as net.minecraft.world.item.ItemStack
            getTagCloneMethod!!.setAccessible(true)
            tag = getTagCloneMethod!!.invoke(nmsItem).toString()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return tag
    }

}
