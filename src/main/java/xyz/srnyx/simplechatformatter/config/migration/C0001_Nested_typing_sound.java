package xyz.srnyx.simplechatformatter.config.migration;

import eu.okaeri.configs.migrate.builtin.NamedMigration;
import xyz.srnyx.annoyingapi.libs.javautilities.manipulation.Mapper;

import static eu.okaeri.configs.migrate.ConfigMigrationDsl.*;


//TODO test
public class C0001_Nested_typing_sound extends NamedMigration {
    public C0001_Nested_typing_sound() {
        super("migrates sound to nested structure inside sound key",
                when(
                        // Only migrate if sound not already nested
                        not(exists("broadcast.title.typing.sound.sound.sound")),
                        multi(
                                // volume: 0 -> enabled: false
                                when(
                                        match("broadcast.title.typing.sound.volume", volume -> Mapper.toInt(volume)
                                                .map(volumeInt -> volumeInt == 0)
                                                .orElse(false)),
                                        supply("broadcast.title.typing.sound.enabled", () -> false),
                                        supply("broadcast.title.typing.sound.enabled", () -> true)),

                                // Nest
                                move("broadcast.title.typing.sound.sound", "broadcast.title.typing.sound.sound.sound"),
                                move("broadcast.title.typing.sound.category", "broadcast.title.typing.sound.sound.category"),
                                move("broadcast.title.typing.sound.volume", "broadcast.title.typing.sound.sound.volume"),
                                move("broadcast.title.typing.sound.pitch", "broadcast.title.typing.sound.sound.pitch"))));
    }
}
