plugins {
    id("cn.dreeam.rsp.wrapper")
}

dependencies {
    compileOnly(project(":Platform:Abstraction"))
    compileOnly(project(":Platform:Paper"))

    compileOnly("dev.folia:folia-api:1.20.4-R0.1-SNAPSHOT")
}
