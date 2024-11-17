import xyz.srnyx.gradlegalaxy.enums.Repository
import xyz.srnyx.gradlegalaxy.enums.repository
import xyz.srnyx.gradlegalaxy.utility.setupAnnoyingAPI
import xyz.srnyx.gradlegalaxy.utility.spigotAPI


plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "1.3.2"
    id("com.gradleup.shadow") version "8.3.5"
}

setupAnnoyingAPI("5.1.3", "xyz.srnyx", "1.1.0", "A very simple plugin to format your chat!")
spigotAPI("1.8.8")

repository(Repository.PLACEHOLDER_API, Repository.DV8TION, Repository.SCARSZ)
dependencies {
    compileOnly("me.clip", "placeholderapi", "2.11.6")
    compileOnly("com.discordsrv", "discordsrv", "1.27.0")
}
