package gg.flyte.event.game.main.type

import gg.flyte.event.ChristmasEvent
import com.learnspigot.event.game.main.MainGame
import com.learnspigot.event.game.main.MainGameEngine
import com.learnspigot.event.util.MapLocation
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.extension.contains
import gg.flyte.twilight.extension.getBlocks
import gg.flyte.twilight.scheduler.repeat
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Boat
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.vehicle.VehicleExitEvent
import org.bukkit.util.BoundingBox

class SledRacingGame : MainGame() {

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
        STARTING_WALL.getBlocks(gg.flyte.event.ChristmasEvent.WORLD).applyForEach { type = Material.OAK_FENCE }

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
    }

    override fun start() {
        startTime = System.currentTimeMillis()

        STARTING_WALL.getBlocks(gg.flyte.event.ChristmasEvent.WORLD).applyForEach { type = Material.AIR }

        var timer = 60.0
        tasks += repeat(5) {
            Bukkit.getOnlinePlayers().applyForEach { sendActionBar(Component.text("$timer seconds")) }

            if (timer == 0.0) {
                Bukkit.broadcastMessage("game ended as end timer reached")
                MainGameEngine.stop()
                return@repeat
            }

            Bukkit.getOnlinePlayers().forEach { player ->
                if (FINISHING_AREA.contains(player.location) && !points.containsKey(player.uniqueId)) {
                    points[player.uniqueId] = -1
                    player.sendMessage("you finished!!! in ${System.currentTimeMillis() - startTime!!}")

                    // MAYBE REPLACE THE FINISH LINE WITH CLEINT SDIE BARRIERS SO CANT GO BACK?
                }
            }

            if (points.size == competingPlayers) {
                Bukkit.broadcastMessage("game ended as all players finished")
                MainGameEngine.stop()
            }

            timer -= 0.25
        }
    }

    override fun stop() {
        boats.values.forEach {
            it.remove()
        }
    }

    override fun onPlayerJoin(player: Player) {
        // TP in as a spectator
    }

    override fun onPlayerQuit(player: Player) {
        boats[player]?.also {
            it.remove()
            boats.remove(player)
        }
    }

}