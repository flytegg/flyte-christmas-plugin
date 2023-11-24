package gg.flyte.event.game.lobby.type

import gg.flyte.event.game.lobby.LobbyGame
import gg.flyte.event.game.lobby.LobbyGameEngine
import gg.flyte.event.util.MapLocation
import gg.flyte.twilight.event.custom.interact.PlayerMainHandInteractEvent
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.applyForEach
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

class PresentHuntGame(override val player: Player) : LobbyGame() {

    companion object {
        private var metadataSet = false

        private val PRESENT_LOCATIONS = listOf(
            MapLocation(1,1,1)
        )
    }

    private val collectedPresents = mutableListOf<Int>()

    override fun events() {
        events += event<PlayerMainHandInteractEvent> {
            clickedBlock?.let {
                if (it.type != Material.PLAYER_HEAD) return@let
                if (!it.hasMetadata("present")) return@let

                val present = it.getMetadata("present").first().asInt()
                if (collectedPresents.contains(present)) return@let

                collectedPresents += present
                player.sendBlockChange(it.location, Material.AIR.createBlockData())

                if (collectedPresents.size == PRESENT_LOCATIONS.size) {
                    player.sendMessage(Component.text("YOU COLLECTEDALL PRESENTS! GAME IS JOEVER"))
                    LobbyGameEngine.stopGame(player, this@PresentHuntGame)
                } else {
                    player.sendMessage(Component.text("present collected"))
                }
            }
        }
    }

    override fun start() {
        if (!metadataSet) {
            PRESENT_LOCATIONS.applyForEach { block.type = Material.PLAYER_HEAD }// Add christmas texture
            metadataSet = true
        }
    }

    override fun stop() {

    }

    override fun onPlayerQuit(player: Player) {
        LobbyGameEngine.stopGame(player, this)
    }

}