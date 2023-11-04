package com.learnspigot.event.engine.game

import com.learnspigot.event.ChristmasEvent
import com.learnspigot.event.engine.Game
import com.learnspigot.event.util.MapLocation
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.getBlocks
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Boat
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import org.spigotmc.event.entity.EntityDismountEvent

class SledRacingGame : Game() {

    private val STARTING_WALL = BoundingBox.of(
        MapLocation(-136, 80, 95),
        MapLocation(-136, 80, 103)
    )

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
        event<EntityDismountEvent> {
            Bukkit.broadcastMessage("sup")
        }
    }

    override fun start() {
        STARTING_WALL.getBlocks(ChristmasEvent.WORLD).forEach {
            it.type = Material.AIR
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