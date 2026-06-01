import xyz.srnyx.gradlegalaxy.data.config.DependencyConfig
import xyz.srnyx.gradlegalaxy.data.config.JavaSetupConfig
import xyz.srnyx.gradlegalaxy.enums.Repository
import xyz.srnyx.gradlegalaxy.enums.repository
import xyz.srnyx.gradlegalaxy.utility.setupAnnoyingAPI
import xyz.srnyx.gradlegalaxy.utility.spigotAPI


plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "2.1.0"
    id("com.gradleup.shadow") version "8.3.9"
}

spigotAPI(config = DependencyConfig(version = "1.8.8"))
setupAnnoyingAPI(javaSetupConfig = JavaSetupConfig(
    group = "xyz.srnyx",
    version = "1.3.0",
    description = "A very simple plugin to format your chat!"),
    annoyingAPIConfig = DependencyConfig(version = "db46b2b"))

repository(Repository.PLACEHOLDER_API, Repository.DV8TION, Repository.SCARSZ)
dependencies {
    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("com.discordsrv:discordsrv:1.27.0")
}
