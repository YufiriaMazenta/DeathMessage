import java.text.SimpleDateFormat

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow").version("7.1.2")
    kotlin("jvm") version "1.9.20"
}

group = "com.github.yufiriamazenta"
version = "1.3.2"

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
    maven("http://repo.crypticlib.com:8081/repository/maven-public/") {
        isAllowInsecureProtocol = true
    }
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.19-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.1")
    compileOnly("io.lumine:Mythic-Dist:5.3.5")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("com.crypticlib:common:0.18.10")
    implementation(kotlin("stdlib-jdk8"))
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
        relocate("crypticlib", "com.github.yufiriamazenta.deathmsg.crypticlib")
        relocate("kotlin", "com.github.yufiriamazenta.deathmsg.libs.kotlin")
        relocate("org.intellij.lang.annotations", "com.github.yufiriamazenta.deathmsg.libs.intellij.lang.annotations")
        relocate("org.jetbrains.annotations", "com.github.yufiriamazenta.deathmsg.libs.jetbrains.annotations")
    }
}
kotlin {
    jvmToolchain(17)
}