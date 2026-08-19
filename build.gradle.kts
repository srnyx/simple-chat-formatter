plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "snapshot"
    id("com.gradleup.shadow") version "9.6.1"
    id("me.modmuss50.mod-publish-plugin") version "675051c"
    id("io.papermc.hangar-publish-plugin") version "0.1.4"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

group = "xyz.srnyx"
description = "A very simple plugin to format your chat!"

galaxy {
    minecraft {
        spigotAPI("1.8.8")
        annoyingAPI("snapshot")

        dependency {
            optional {
                repositories.add(PLACEHOLDER_API)
                group = "me.clip"
                artifact = "placeholderapi"
                version = "2.12.2"

                pluginYml = "PlaceholderAPI"
                modrinth = "placeholderapi"
                hangar = "PlaceholderAPI"
            }

            optional {
                repositories.add(SCARSZ)
                group = "com.discordsrv"
                artifact = "discordsrv"
                version = "1.27.0"

                pluginYml = "DiscordSRV"
                modrinth = "discordsrv"
                hangar = "DiscordSRV"
            }
        }

        pluginYml {
            developerData(SRNYX)
            permissionPrefix = "chat"

            command("chatreload") {
                aliases.addAll("cr", "scfreload")
                description = "Reloads the plugin"

                permission("reload") {
                    description = "Allows the player to use /chatreload"
                }
            }
            command("broadcast") {
                aliases.addAll("bc")
                description = "Broadcasts a message to the entire server"

                permission("broadcast") {
                    description = "Allows the player to use /broadcast"
                }
            }
            command("broadcasttoggle") {
                aliases.addAll("bctoggle")
                description = "Toggles all messages being broadcasted"

                permission("broadcast.toggle") {
                    description = "Allows the player to use /broadcasttoggle"
                }
            }
            command("clearchat") {
                aliases.addAll("cc")
                description = "Clears the chat"

                permission("clear") {
                    description = "Allows the player to use /clearchat"
                }
            }

            permission("broadcast.toggle.other") {
                description = "Allows the player to use /broadcasttoggle on others"
            }
            permission("clear.bypass") {
                description = "Allows the player to bypass the chat clear"
            }
            permission("format") {
                description = "Allows the player to use formatting codes"
            }
            permission("spam.bypass") {
                description = "Allows the player to bypass the chat spam filter"
            }
            permission("filter.bypass") {
                description = "Allows the player to bypass the chat filter"
            }
            permission("filter.see") {
                description = "Allows the player to see filtered messages"
            }
        }

        platformPublishing {
            github("srnyx/simple-chat-formatter")
            modrinth("h3dHO3Sr")
            hangar("SimpleChatFormatter")
            spigot("111804")
            curseforge("867282")

            projectData("simple-chat-formatter")
        }
    }
}
