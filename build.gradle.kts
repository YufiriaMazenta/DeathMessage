import java.text.SimpleDateFormat

plugins {
    `java-library`
    `maven-publish`
    id("io.github.goooler.shadow").version("8.1.7")
    id("org.jetbrains.kotlin.jvm") version "2.4.20-Beta2"
}

group = "pers.yufiria"
version = rootProject.findProperty("pluginVer").toString()

repositories {
    mavenLocal()
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    //CrypticLib
    maven("https://repo.crypticlib.incrafttime.top/repository/maven-public/")
    mavenCentral()
}

dependencies {
//    compileOnly("io.papermc.paper:paper-core:1.21.11")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("io.lumine:Mythic-Dist:5.3.5")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("com.crypticlib:bukkit:${rootProject.property("crypticlibVer")}")
    compileOnly(kotlin("stdlib"))
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    val props = HashMap<String, String>()
    val pluginVersion: String = version.toString() + "-" + SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis())
    props["version"] = pluginVersion
    processResources {
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    compileJava {
        options.encoding = "UTF-8"
    }
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("DeathMessage-$version.jar")
        relocate("crypticlib", "pers.yufiria.deathmsg.crypticlib")
        relocate("kotlin", "kotlin2420")
        relocate("org.intellij.lang.annotations", "pers.yufiria.deathmsg.libs.intellij.lang.annotations")
        relocate("org.jetbrains.annotations", "pers.yufiria.deathmsg.libs.jetbrains.annotations")
    }
}
kotlin {
    jvmToolchain(17)
}