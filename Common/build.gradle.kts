plugins {
    id("cn.dreeam.rsp.wrapper")
    id("com.gradleup.shadow") version "8.3.5"
}

val adventureVersion = "4.17.0"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT")

    implementation(project(":Platform:Abstraction"))
    implementation(project(":Platform:Folia"))
    implementation(project(":Platform:Paper"))
    implementation(project(":Platform:Spigot"))

    compileOnly("org.apache.logging.log4j:log4j-api:2.24.1")
    compileOnly("it.unimi.dsi:fastutil:8.5.15")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT") // Remove
    implementation("com.github.technicallycoded:FoliaLib:0.4.3")
    implementation("com.github.thatsmusic99:ConfigurationMaster-API:v2.0.0-rc.2")

    compileOnly("net.essentialsx:EssentialsX:2.20.1")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")

    api("net.kyori:adventure-platform-bukkit:4.3.4")
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

