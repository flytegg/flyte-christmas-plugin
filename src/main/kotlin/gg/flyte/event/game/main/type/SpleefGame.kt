package gg.flyte.event.game.main.type

import gg.flyte.event.ChristmasEvent
import gg.flyte.event.game.GameType
import gg.flyte.event.game.main.MainGame
import gg.flyte.event.game.main.MainGameEngine
import gg.flyte.event.game.main.MainGameState
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.*
import net.kyori.adventure.text.Component.text
import org.bukkit.*
import org.bukkit.block.data.type.Snow
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack

class SpleefGame : MainGame(GameType.SPLEEF) {

    private val shovel = ItemStack(Material.DIAMOND_SHOVEL).apply { name("${ChatColor.RED}Scooper") }
    private val snowball = ItemStack(Material.SNOWBALL)

    override fun events() {
        events += event<BlockBreakEvent>(ignoreCancelled = true) {
            if (state != MainGameState.ACTIVE) return@event
            if (block.type != Material.SNOW) return@event

            isCancelled = true
            isDropItems = false

            var clear = false

            (block.blockData as Snow).apply {
                if (layers == 1) {
                    clear = true
                    return@apply
                }
                layers -= 1
            }.also { block.blockData = it }

            if (clear) block.type = Material.AIR
            if (player.inventory.itemInMainHand == shovel) player.inventory.addItem(snowball)
        }

        events += event<PlayerMoveEvent>(ignoreCancelled = true) {
            if (player.isOnFire()) handleElimination(player)
        }
    }

    // Setup arena
    init {
        type.region.getBlocks(ChristmasEvent.WORLD).applyForEach {
            type = Material.SNOW
            (blockData as Snow).apply { layers = maximumLayers }.also { blockData = it }
        }
    }

    override fun start() {
        alive.applyForEach {
            inventory.addItem(shovel)
            gameMode = GameMode.SURVIVAL
        }
    }

    private fun handleElimination(player: Player) {
        player.apply {
            alive -= this
            playSound(Sound.ENTITY_PLAYER_DEATH)
            teleport(this@SpleefGame.type.spectatorSpawn!!)
            fireTicks = 0
            Bukkit.broadcast(text("$name was eliminated"))
        }
        if (alive.size <= 1) MainGameEngine.stop()
    }

    override fun stop() {
        type.region.getBlocks(ChristmasEvent.WORLD).applyForEach {
            type = Material.SPRUCE_PLANKS
        }
    }

    override fun onPlayerJoin(player: Player) {

    }

    override fun onPlayerQuit(player: Player) {

    }
}