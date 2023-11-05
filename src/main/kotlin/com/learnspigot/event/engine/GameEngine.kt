package com.learnspigot.event.engine

import com.learnspigot.event.ChristmasEvent.Companion.LOBBY_SPAWN
import gg.flyte.twilight.event.TwilightListener
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.extension.clearActionBar
import gg.flyte.twilight.scheduler.repeat
import gg.flyte.twilight.time.TimeUnit
import net.kyori.adventure.text.Component.*
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.time.Duration
import java.util.*

object GameEngine {

    var type: GameType? = null
    var game: Game? = null

    val points = mutableMapOf<UUID, Int>()

    var countdownTask: BukkitTask? = null

    /**
     * Initiates the game, including player teleportation, game initialization, and countdown before starting.
     */
    fun start() {
        Bukkit.getOnlinePlayers().applyForEach { teleport(this@GameEngine.type!!.spawns.random()) }

        game = (type!!.clazz.getDeclaredConstructor().newInstance() as Game).apply { events() }

        Bukkit.broadcast(
            text().append(
                CHAT_SPLITTER,
                newline(),
                type!!.title, text(" - Game starting!"),
                newline(),
                type!!.description,
                newline(),
                CHAT_SPLITTER
            ).build()
        )

        var countdown = 3
        countdownTask = repeat(10, 1, TimeUnit.SECONDS) {
            val title = countdownTitleFor(countdown)
            Bukkit.getOnlinePlayers().applyForEach {
                title?.let { showTitle(it) }
                playSound(
                    location,
                    if (countdown == 0) Sound.ENTITY_PLAYER_LEVELUP else Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
                    1.0f,
                    1.0f
                )
            }

            if (countdown == 0) {
                cancel()
                game!!.state = GameState.ACTIVE
                game!!.start()
            }

            countdown--
        }
    }

    /**
     * Stops the game, teleporting online players to the lobby, canceling the countdown task, and ending the game.
     */
    fun stop() {
        Bukkit.getOnlinePlayers().applyForEach {
            sendMessage("balls")
            teleport(LOBBY_SPAWN)
            clearTitle()
            clearActionBar()
            sendMessage("ballz")
        }

        type = null

        countdownTask?.let {
            it.cancel()
            countdownTask = null
        }

        game?.let {
            it.stop()
            it.tasks.forEach(BukkitTask::cancel)
            it.events.forEach(TwilightListener::unregister)
            it.points.forEach { (key, value) ->
                points.merge(key, value) { existingValue, newValue ->
                    existingValue + newValue
                }
            }
            game = null
        }

        Bukkit.broadcast(
            text().append(
                CHAT_SPLITTER,
                newline(),
                text("Game ended!"),
                newline(),
                CHAT_SPLITTER
            ).build()
        )
    }

    /**
     * Handles a player joining the game, including point initialization and teleportation to the lobby.
     *
     * @param player The Player object representing the joining player.
     */
    fun onPlayerJoin(player: Player) {
        points.getOrPut(player.uniqueId) { 1 }
        game?.apply { onPlayerJoin(player) } ?: player.teleport(LOBBY_SPAWN)
    }

    fun onPlayerQuit(player: Player) {
        game?.onPlayerQuit(player)

        // Check here to see if too many players left for game to continue running
    }

    /**
     * Generates a Title object for a countdown with a specified number of seconds.
     *
     * @param seconds The number of seconds in the countdown.
     * @return A Title object representing the countdown title, or null if the seconds value is not 0, 1, 2, or 3.
     */
    private fun countdownTitleFor(seconds: Int): Title? {
        val times = Title.Times.times(Duration.ZERO, Duration.ofMillis(1500), Duration.ZERO)
        return when (seconds) {
            3 -> Title.title(text("➌", NamedTextColor.RED), text("seconds"), times)
            2 -> Title.title(text("➋", NamedTextColor.GOLD), text("seconds"), times)
            1 -> Title.title(text("➊", NamedTextColor.DARK_GREEN), text("second"), times)
            0 -> Title.title(text("GO", NamedTextColor.GREEN, TextDecoration.BOLD), empty(), times)
            else -> null
        }
    }

}

val CHAT_SPLITTER = text(
    "                                                                               ",
    NamedTextColor.GRAY,
    TextDecoration.STRIKETHROUGH
)