package gg.flyte.event.game.lobby

import gg.flyte.event.game.Game
import org.bukkit.entity.Player

abstract class LobbyGame : Game() {

    abstract val player: Player

}