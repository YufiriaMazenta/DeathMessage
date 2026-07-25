package pers.yufiria.deathmsg

import crypticlib.BukkitPlugin
import crypticlib.chat.BukkitMsgSender
import crypticlib.dependency.Dependency
import crypticlib.dependency.DependencyLoader
import crypticlib.util.IOHelper
import org.bukkit.Bukkit

class DeathMessage: BukkitPlugin() {

    init {
        DependencyLoader.INSTANCE.loadDependency(
            Dependency
                .builder("org.jetbrains.kot#lin", "kot#lin-stdlib", "2.4.20-Beta2")
                .repository(Dependency.REPOSITORY_MAVEN_CENTRAL_MIRROR_ALI)
                .repository(Dependency.REPOSITORY_MAVEN_CENTRAL)
                .test("!kot#lin2420%Kot#linVersion")
                .relocate("kot#lin", "kot#lin2420")
                .relocate("org%intellij%lang%annotations", "pers%yufiria%deathmsg%libs%intellij%lang%annotations")
                .relocate("org%jetbrains%annotations", "pers%yufiria%deathmsg%libs%jetbrains%annotations")
                .build()
        )
    }

    override fun whenEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHooker.register()
            IOHelper.info("PlaceholderExpansion registered")
        }
        BukkitMsgSender.INSTANCE.info("DeathMessage Enabled")
    }

    override fun whenDisable() {
        BukkitMsgSender.INSTANCE.info("DeathMessage Disabled")
    }

}

val DEATH_MESSAGE: DeathMessage = (Bukkit.getPluginManager().getPlugin("DeathMessage") as DeathMessage?)!!