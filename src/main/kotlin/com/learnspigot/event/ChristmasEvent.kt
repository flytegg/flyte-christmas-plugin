package com.learnspigot.event

import com.learnspigot.event.debug.DebugCommand
import com.learnspigot.event.game.GameCommand
import com.learnspigot.event.game.GameType
import com.learnspigot.event.listener.ConnectionListener
import com.learnspigot.event.listener.MapListener
import com.learnspigot.event.util.MapLocation
import com.learnspigot.event.util.npc.NPCListener
import gg.flyte.twilight.event.custom.admin.listener.OpEventListener
import gg.flyte.twilight.event.disableCustomEventListeners
import gg.flyte.twilight.extension.enumValue
import gg.flyte.twilight.twilight
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.exception.CommandErrorException
import revxrsal.commands.ktx.autoCompleter

class ChristmasEvent : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: ChristmasEvent
        lateinit var SERVER: Server
        lateinit var WORLD: World
        lateinit var LOBBY_SPAWN: MapLocation
    }

    override fun onEnable() {
        INSTANCE = this
        SERVER = server
        WORLD = Bukkit.getWorld("world")!!
        LOBBY_SPAWN = MapLocation(-134, 80, 78, 90, 0)

        twilight(this)
        disableCustomEventListeners(OpEventListener)

        listeners()
        commands()

        // TEST REMOVE
        /*NPC(
            Location(WORLD, -22.5, 100.0, 4.5),
            Skin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY2MzcyMzgwNTQ4NywKICAicHJvZmlsZUlkIiA6ICIwNTkyNTIxZGNjZWE0NzRkYjE0M2NmMDg2MDA1Y2FkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJwdXIyNCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kMGNhM2VlNGJlYTgxNTA5ZTQ5MjI5ODVlY2YyNmQ0N2MwODVkYWY1ODUzMDc1MDBlMTZhYmVlMDE5YjlkZTUxIgogICAgfQogIH0KfQ==",
                "YIS3T3N/jF+T8SpPDS/bCZHly8GFmmDiahpUgaJedKNXfcgaUDjIl6mTCiMQITYFdM0Q3UKNKr9LIt7rSgwPUjCEdmjwfpBG0yRAXoQ9rG30JdtvzpKqNGLU8t9LQfkHfqah9ti3T9gh6n6G4KIdTyBWW+xVc1Nb/y5WU0PtnK/W7274hKvlrxswfxqcO4M67PqutFHwkk3F0FyG5b2HfDpUdM3DMP9qDwGA2OlhmZLuX4R5/aLvGAfetl3YHzFVfZggZOtEkaTetv42gXHnxfH9UKkHrzwtcQ5ms75Jz2Nv1oJnca6Ya6bEEfeo9dRQajzJOAGrTlnPSvatEKSqwmV3md/J2J0cq37koUvkWBCkeKthzpkZX+CqANyGmU7BzV9GvL7Lgre3vx9GHIfdafbMk/75LL7xco/nsO5ZHoO0v5cxRP3eiW/sSnaDwpfTMUyiWeqdFU+SeJIALtcYbV398m0sCd5yruXDk7iqS12oTMoHNHDKf8qFzev+RA6s8l/Ct9dAT/DxK5Cy2nf9tNpqzSKVZRx501cECI+JNsNbIMaXE8m0lORaek+vd0jDN8LNoTN3mXZn5zHTxp49dtp9t0NfcXCIfUyKL6J8M8S8MtWvzjgpgiS95o7OHMgPhJS4BUK9Xogitn7Lrt0qyclH//JteKHmY3FWgoAxOVA="
            ),
            true,
            "test"
        )*/
        // TEST REMOVE
    }

    override fun onDisable() {

    }

    private fun commands() {
        BukkitCommandHandler.create(this).apply {
            register(
                GameCommand,
                DebugCommand
            )

            registerValueResolver(GameType::class.java) {
                enumValue<GameType>(it.pop()) ?: throw CommandErrorException("Invalid game type!", it.pop())
            }

            registerValueResolver(DebugCommand.WorldName::class.java) { DebugCommand.WorldName(it.pop()) }

            autoCompleter {
                registerParameterSuggestions(GameType::class.java) { _, _, _ ->
                    GameType.entries.map { it.name.uppercase() }
                }

                registerParameterSuggestions(DebugCommand.WorldName::class.java) { _, _, _ ->
                    Bukkit.getWorlds().map { it.name }
                }
            }
        }
    }

    private fun listeners() {
        ConnectionListener
        NPCListener
        MapListener
    }

}