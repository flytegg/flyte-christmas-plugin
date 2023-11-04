package com.learnspigot.event.engine.game

import com.learnspigot.event.ChristmasEvent
import com.learnspigot.event.engine.Game
import com.learnspigot.event.engine.GameEngine
import com.learnspigot.event.engine.GameState
import com.learnspigot.event.util.MapLocation
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.extension.contains
import gg.flyte.twilight.extension.getBlocks
import gg.flyte.twilight.extension.getLocations
import gg.flyte.twilight.scheduler.repeat
import gg.flyte.twilight.time.TimeUnit
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Boat
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.vehicle.VehicleExitEvent
import org.bukkit.util.BoundingBox

class SledRacingGame : Game() {

    private val STARTING_WALL = BoundingBox.of(
        MapLocation(-136, 80, 95),
        MapLocation(-136, 80, 103)
    )

    private val FINISHING_AREA = BoundingBox.of(
        MapLocation(-174, 83, 103),
        MapLocation(-170, 80, 95)
    )

    private var startTime: Long? = null
    private var competingPlayers = Bukkit.getOnlinePlayers().size

    private val boats = mutableMapOf<Player, Boat>()

    init {
        STARTING_WALL.getBlocks(ChristmasEvent.WORLD).forEach {
            it.type = Material.OAK_FENCE
        }

        Bukkit.getOnlinePlayers().forEach { player ->
            (player.world.spawnEntity(player.location, EntityType.BOAT) as Boat).apply {
                isInvulnerable = true
                addPassenger(player)
                boats[player] = this
            }
        }


    }

    override fun events() {
        events += event<VehicleExitEvent> {
            isCancelled = true
        }

        events += event<EntityDamageEvent> {
            if (entityType == EntityType.BOAT) isCancelled = true
        }

        events += event<PlayerMoveEvent> { // Won't lag, very simple maths check
            if (state == GameState.ACTIVE && FINISHING_AREA.contains(player.location) && !points.containsKey(player.uniqueId)) {
                points[player.uniqueId] = -1
                player.sendMessage("you finished!!! in ${System.currentTimeMillis() - startTime!!}")

                val barrierBlockData = Material.BARRIER.createBlockData()
                STARTING_WALL.getLocations(ChristmasEvent.WORLD).forEach {
                    player.sendBlockChange(it, barrierBlockData)
                }

                if (points.size == competingPlayers) {
                    Bukkit.broadcastMessage("game ended as all players finished")
                    GameEngine.stop()
                }
            }
        }
    }

    override fun start() {
        startTime = System.currentTimeMillis()

        STARTING_WALL.getBlocks(ChristmasEvent.WORLD).forEach {
            it.type = Material.AIR
        }

        var timer = 60
        tasks += repeat(1, TimeUnit.SECONDS) {
            if (timer == 0) {
                Bukkit.broadcastMessage("game ended as end timer reached")
                GameEngine.stop()
                return@repeat
            }

            Bukkit.getOnlinePlayers().applyForEach { sendActionBar(Component.text("$timer seconds")) }
            timer--
        }
    }

    override fun stop() {
        boats.values.forEach {
            it.remove()
        }


    }

    override fun onPlayerJoin(player: Player) {

    }

    override fun onPlayerQuit(player: Player) {
        boats[player]?.also {
            it.remove()
            boats.remove(player)
        }



    }

}