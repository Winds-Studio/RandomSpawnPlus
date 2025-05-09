pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        maven("https://repo.auxilor.io/repository/maven-public/")
    }
}

rootProject.name = "randomspawnplus"

include(
    ":Common",

    ":Platform:Abstraction",
    ":Platform:Folia",
    ":Platform:Paper",
    ":Platform:Spigot",
)