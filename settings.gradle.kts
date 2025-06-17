pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
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
