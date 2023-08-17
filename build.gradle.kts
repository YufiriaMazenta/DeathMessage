import java.text.SimpleDateFormat

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow").version("7.1.2")
}

repositories {
    mavenLocal()
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.19-R0.1-SNAPSHOT")
    compileOnly("dev.folia:folia-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
    implementation("com.github.YufiriaMazenta:CrypticLib:1.0.1")
}

group = "com.github.yufiriamazenta"
version = "1.0.9"
description = "DeathMessage"
java.sourceCompatibility = JavaVersion.VERSION_17
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    val props = HashMap<String, String>()
    val pluginVersion: String = version.toString() + "-" + SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis())
    props["version"] = pluginVersion
    "processResources"(ProcessResources::class) {
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    compileJava {
        options.encoding = "UTF-8"
    }
    shadowJar {
        archiveFileName.set("DeathMessage-$version.jar")
        relocate("crypticlib", "com.github.yufiriamazenta.crypticlib")
    }
}