package gg.flyte.event.util

import gg.flyte.event.visual.Board
import gg.flyte.twilight.scheduler.delay
import gg.flyte.twilight.scheduler.repeat
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.scoreboard.*
import org.bukkit.scoreboard.Scoreboard

abstract class Scoreboard(open val player: Player) : Listener {
    val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    val objective: Objective = scoreboard.registerNewObjective("board", Criteria.DUMMY, title())
    val team: Team = scoreboard.getTeam("no_collide") ?: scoreboard.registerNewTeam("no_collide")

    init {
        delay {
            objective.displaySlot = DisplaySlot.SIDEBAR
            objective.displayName = title().plainText

            lines(player).reversedArray().forEachIndexed { index, line ->
                scoreboard.registerNewTeam((index + 1).toString()).apply {
                    val value = ChatColor.entries[index].toString()
                    addEntry(value)
                    prefix = line.plainText
                    objective.getScore(value).score = index + 1
                }
            }

            player.scoreboard = scoreboard
        }

        repeat(2) { update() }
    }

    private fun update() {
        lines(player).reversedArray().forEachIndexed { index, line ->
            val lineString = line.plainText
            scoreboard.getTeam((index + 1).toString())?.let { if (it.hasLineChanged(lineString)) it.prefix = lineString }
        }
    }

    private fun Team.hasLineChanged(line: String): Boolean = ChatColor.stripColor(prefix) != line

    abstract fun title(): TextComponent
    abstract fun lines(player: Player): Array<TextComponent>

    fun enableCollisions() {
        if (team.hasEntry(player.name)) team.removeEntry(player.name)
    }

    fun disableCollisions() {
        if (!team.hasEntry(player.name)) team.addEntry(player.name)
    }

    companion object {
        private val boards = mutableMapOf<Player, gg.flyte.event.util.Scoreboard>()

        fun onPlayerJoin(player: Player) {
            boards += player to Board(player)
        }

        fun onPlayerQuit(player: Player) {
            boards -= player
        }
    }
}