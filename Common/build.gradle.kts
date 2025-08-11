plugins {
    id("cn.dreeam.rsp.wrapper")
    id("com.gradleup.shadow") version "9.0.1"
}

val adventureVersion = "4.24.0"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    implementation(project(":Platform:Abstraction"))
    implementation(project(":Platform:Folia"))
    implementation(project(":Platform:Paper"))
    implementation(project(":Platform:Spigot"))

    compileOnly("org.apache.logging.log4j:log4j-api:2.25.1")
    compileOnly("it.unimi.dsi:fastutil:8.5.16")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT") // TODO: Remove
    implementation("com.tcoded:FoliaLib:0.5.1")
    implementation("com.github.thatsmusic99:ConfigurationMaster-API:v2.0.0-rc.3")

    compileOnly("net.essentialsx:EssentialsX:2.21.2") {
        // XD
        exclude("org.spigotmc", "spigot-api")
        exclude("io.papermc.paper", "paper-api")
    }
    compileOnly("net.luckperms:api:5.5")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")

    api("net.kyori:adventure-platform-bukkit:4.4.1")
    api("net.kyori:adventure-api:$adventureVersion")
    api("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
}

tasks {
    build.configure {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.${archiveExtension.get()}"
        exclude("META-INF/**") // Dreeam - Avoid to include META-INF/maven in Jar
        minimize {
            exclude(dependency("com.tcoded.folialib:.*:.*"))
        }
        relocate("net.kyori", "${project.group}.libs.kyori")
        relocate("io.github.thatsmusic99.configurationmaster", "${project.group}.libs.configurationmaster")
        relocate("org.bstats", "${project.group}.libs.bstats")
        relocate("com.tcoded.folialib", "${project.group}.libs.folialib")
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
