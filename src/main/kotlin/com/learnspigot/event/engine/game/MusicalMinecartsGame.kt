package com.learnspigot.event.engine.game

import com.learnspigot.event.ChristmasEvent
import com.learnspigot.event.engine.Game
import com.learnspigot.event.engine.GameEngine
import com.learnspigot.event.engine.GameType
import com.learnspigot.event.util.MapLocation
import com.learnspigot.event.util.NBSSongType
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.*
import gg.flyte.twilight.scheduler.delay
import gg.flyte.twilight.scheduler.repeat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.event.vehicle.VehicleExitEvent
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.BoundingBox
import kotlin.math.roundToInt
import kotlin.random.Random

class MusicalMinecartsGame : Game() {

    private val MINECART_SPAWN = BoundingBox.of(
        MapLocation(-147, 86, 71),
        MapLocation(-133, 86, 58)
    )

    private lateinit var music: RadioSongPlayer

    private val alive = mutableListOf<Player>()
    private val inMinecart = mutableListOf<Player>()
    private val minecarts = mutableListOf<Minecart>()

    private var ticker: BukkitTask? = null

    override fun events() {
        events += event<VehicleEnterEvent> {
            if (vehicle.type == EntityType.MINECART && entered.type == EntityType.PLAYER) {
                inMinecart += entered as Player
                if (inMinecart.size == minecarts.size) {
                    endRound()
                }
            }
        }

        events += event<VehicleExitEvent> {
            if (vehicle.type == EntityType.MINECART && exited.type == EntityType.PLAYER) {
                inMinecart -= exited as Player
            }
        }
    }

    override fun start() {
        val song = NBSSongType.entries.random()
        music = RadioSongPlayer(song.song)
        music.repeatMode = RepeatMode.ALL
        Bukkit.getOnlinePlayers().forEach(music::addPlayer)

        Bukkit.broadcast(
            text().append(
                Component.newline(),
                text("♫ ").color(NamedTextColor.RED), text("Now playing ${song.title}..."),
                Component.newline(),
            ).build()
        )

        alive.addAll(Bukkit.getOnlinePlayers())

        newRound()
    }

    private fun newRound() {
        music.isPlaying = true

        inMinecart.clear()

        tasks += delay((Random.nextInt(5) + 10) * 20) {
            music.isPlaying = false

            Bukkit.getOnlinePlayers().applyForEach { playSound(Sound.BLOCK_NOTE_BLOCK_BASEDRUM) }

            // SPAWNING MINECARTS
            val potentialSpawnLocations = MINECART_SPAWN.getLocations(ChristmasEvent.WORLD)
            kotlin.repeat((alive.size * 0.66).roundToInt()) {
                minecarts += potentialSpawnLocations.random().spawnEntity(EntityType.MINECART) as Minecart
            }

            // TICKER
            var iter = 40
            tasks += repeat(5, 5) {
                if (iter == 0) {
                    endRound()
                    return@repeat
                }

                val component = text()
                for (i in 1 until 41) {
                    component.append(text('|').color(if (i >= iter) NamedTextColor.GRAY else NamedTextColor.GREEN))
                }

                Bukkit.getOnlinePlayers().applyForEach {
                    sendActionBar(component)
                    playSound(Sound.BLOCK_STONE_BUTTON_CLICK_ON)

                    Bukkit.broadcastMessage("${vehicle?.type}")
                }

                Bukkit.broadcastMessage(inMinecart.toString())

                iter--
            }.also { ticker = it }
        }
    }

    private fun endRound() { // When either all Minecarts are filled, or 10 second timer over
        ticker?.cancel()
        Bukkit.getOnlinePlayers().applyForEach { clearActionBar() }

        // ADD SCORES AND REMOVE PLAYERS NOT IN MINECART
        val iterator = alive.iterator()
        while (iterator.hasNext()) {
            val player = iterator.next()
            if (inMinecart.contains(player)) {
                points[player.uniqueId] = points.getOrDefault(player.uniqueId, 0) + 1

                Bukkit.broadcastMessage("$player is not eliminated")
            } else {
                player.teleport(GameType.MUSICAL_MINECARTS.spectatorSpawn!!)
                player.playSound(Sound.ENTITY_PLAYER_DEATH)
                iterator.remove()

                Bukkit.broadcastMessage("$${player.name} was eliminateddd")
            }
        }

        minecarts.applyForEach { remove() }
        minecarts.clear()

        if (alive.size <= 1) {
            music.destroy()
            delay {
                GameEngine.stop()
            }
        } else {
            newRound()

            Bukkit.broadcastMessage("yo")
        }
//
//        minecarts.applyForEach { remove() }
//        minecarts.clear()
    }

    override fun stop() {
//        minecarts.applyForEach {
//            remove()
//        }
    }

    override fun onPlayerJoin(player: Player) {

    }

    override fun onPlayerQuit(player: Player) {

    }

}