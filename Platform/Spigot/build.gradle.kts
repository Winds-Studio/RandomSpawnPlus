plugins {
    id("cn.dreeam.rsp.wrapper")
}

dependencies {
    compileOnly(project(":Platform:Abstraction"))

    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    compileOnly("it.unimi.dsi:fastutil:8.5.15")
}
