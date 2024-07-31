package pers.yufiria.deathmsg.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer

object ComponentUtil {

    val serializer = JSONComponentSerializer.json()

    fun serialize(component: Component): String {
        return serializer.serialize(component)
    }

    fun deserialize(componentStr: String): Component {
        return serializer.deserialize(componentStr)
    }

}