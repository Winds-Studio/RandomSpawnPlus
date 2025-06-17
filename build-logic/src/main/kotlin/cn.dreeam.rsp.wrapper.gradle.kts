plugins {
    `java-library`
    `maven-publish`
}

group = "systems.kscott.randomspawnplus"
version = "6.0.0"

repositories {
    mavenCentral()

    // PaperMC
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    // EssentialsX
    maven {
        name = "ess-repo"
        url = uri("https://repo.essentialsx.net/releases/")
    }

    // acf-paper
    maven {
        name = "aikar-repo"
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }

    // ConfigurationMaster API
    maven {
        name = "ConfigurationMaster-repo"
        url = uri("https://repo.bsdevelopment.org/releases/")
    }

    // JitPack
    maven {
        name = "jitpack.io"
        url = uri("https://jitpack.io/")
    }

    // FoliaLib
    maven {
        name = "devmart-other"
        url = uri("https://nexuslite.gcnt.net/repos/other/")
    }
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand(
                "version" to project.version
            )
        }
    }
}
