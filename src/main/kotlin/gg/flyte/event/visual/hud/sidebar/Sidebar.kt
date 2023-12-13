package gg.flyte.event.visual.hud.sidebar

import gg.flyte.event.util.CHRISTMAS_RED
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.*

object Sidebar {

    val activeScoreboards = hashMapOf<UUID, Scoreboard>()

    fun register(player: Player) {
        player.scoreboard = Bukkit.getScoreboardManager().newScoreboard.apply {
            activeScoreboards[player.uniqueId] = this

            registerNewObjective(
                "sidebar", "dummy", Component
                    .text("\"Flyte Christmas Event 2023\"")
                    .color(CHRISTMAS_RED)
            )
                .apply {
                    displaySlot = DisplaySlot.SIDEBAR

                }
        }
    }

    fun unregister(player: Player) {

    }
}