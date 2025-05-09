plugins {
    id("cn.dreeam.rsp.wrapper")
}

dependencies {
    compileOnly(project(":Platform:Abstraction"))

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}
