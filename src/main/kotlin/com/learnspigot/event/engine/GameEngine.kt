package com.learnspigot.event.engine

import com.learnspigot.event.ChristmasEvent
import com.learnspigot.event.ChristmasEvent.Companion.LOBBY_SPAWN
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.time.Duration
import java.util.*

object GameEngine {

    var type: GameType? = null
    var game: Game? = null

    val points = mutableMapOf<UUID, Int>()

    var countdownTask: BukkitTask? = null

    fun start() {
        Bukkit.getOnlinePlayers().forEach {
            it.teleport(type!!.spawns.random())
        }

        game = type!!.clazz.getDeclaredConstructor().newInstance()

        Bukkit.broadcast(Component.text()
            .append(Component.text("                                                                               ").decorate(TextDecoration.STRIKETHROUGH))
            .appendNewline()
            .append(type!!.title).append(Component.text(" game starting..."))
            .append(type!!.description)
            .appendNewline()
            .append(Component.text("                                                                               ").decorate(TextDecoration.STRIKETHROUGH))
            .build())

        countdownTask = object : BukkitRunnable() {
            var timer = 3
            override fun run() {
                when (timer) {
                    in 1..3 -> {
                        val (titleComponent, subtitleComponent) = when (timer) {
                            3 -> Pair(Component.text("➌").color(NamedTextColor.RED), Component.text("seconds"))
                            2 -> Pair(Component.text("➋").color(NamedTextColor.GOLD), Component.text("seconds"))
                            1 -> Pair(Component.text("➊").color(NamedTextColor.DARK_GREEN), Component.text("second"))
                            else -> null
                        }!!
                        val title = Title.title(titleComponent, subtitleComponent, Title.Times.times(Duration.ZERO, Duration.ofMillis(1500), Duration.ZERO))
                        Bukkit.getOnlinePlayers().forEach {
                            it.showTitle(title)
                            it.playSound(it.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                        }

                        timer--
                    }
                    0 -> {
                        Bukkit.getOnlinePlayers().forEach {
                            it.playSound(it.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                            it.showTitle(Title.title(Component.text("GO").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD), Component.empty(), Title.Times.times(Duration.ZERO, Duration.ofMillis(1500), Duration.ZERO)))
                        }

                        cancel()
                        game!!.start()
                    }
                }
            }
        }.runTaskTimer(ChristmasEvent.INSTANCE, 200L, 20L)
    }

    fun stop() {
        Bukkit.getOnlinePlayers().forEach {
            it.teleport(LOBBY_SPAWN)
        }

        type = null

        countdownTask?.let {
            it.cancel()
            countdownTask = null
        }

        game?.let {
            it.stop()
            it.tasks.forEach(BukkitTask::cancel)
            game = null
        }

        Bukkit.broadcast(Component.text()
            .append(Component.text("                                                                               ").decorate(TextDecoration.STRIKETHROUGH))
            .appendNewline()
            .append(Component.text("game ended"))
            .appendNewline()
            .append(Component.text("                                                                               ").decorate(TextDecoration.STRIKETHROUGH))
            .build())
    }

    fun onPlayerJoin(player: Player) {
        points.getOrPut(player.uniqueId) { 1 }
        game?.apply { onPlayerJoin(player) } ?: player.teleport(LOBBY_SPAWN)
    }

    fun onPlayerQuit(player: Player) {
        game?.onPlayerQuit(player)

        // Check here to see if too many players left for game to continue running
    }

}