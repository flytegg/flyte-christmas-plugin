package gg.flyte.event

import gg.flyte.event.debug.DebugCommand
import gg.flyte.event.game.GameCommand
import gg.flyte.event.game.GameType
import gg.flyte.event.listener.ConnectionListener
import gg.flyte.event.listener.MapListener
import gg.flyte.event.util.MapLocation

import gg.flyte.event.util.npc.NPCListener
import gg.flyte.twilight.event.custom.admin.listener.OpEventListener
import gg.flyte.twilight.event.disableCustomEventListeners
import gg.flyte.twilight.extension.enumValue
import gg.flyte.twilight.time.TimeUnit
import gg.flyte.twilight.twilight
import org.bukkit.*
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.exception.CommandErrorException
import revxrsal.commands.ktx.autoCompleter

class ChristmasEvent : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: ChristmasEvent
        lateinit var SERVER: Server
        lateinit var WORLD: World
        lateinit var LOBBY_SPAWN: Location
    }

    override fun onEnable() {
        worlds()
        INSTANCE = this
        SERVER = server
        WORLD = Bukkit.getWorld("world")!!
        LOBBY_SPAWN = Location(Bukkit.getWorld("build"), -559.5, 105.5, 554.5, 170F, -5F)

        twilight(this)
        disableCustomEventListeners(OpEventListener)

        listeners()
        CommandManager

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

        //
        gg.flyte.twilight.scheduler.repeat(450,  TimeUnit.TICKS) {
            Bukkit.getPlayer("dombogo")?.let { player ->
                player.teleport(player.location.subtract(1.0, 0.0, 0.0))
            }
        }
        //

        // TEST REMOVE
    }

    private fun worlds() {
        Bukkit.createWorld(WorldCreator("build"))
        Bukkit.createWorld(WorldCreator("tree"))
    }

    private fun listeners() {
        ConnectionListener
        NPCListener
        MapListener
    }

}