package gg.flyte.event.game.main

import gg.flyte.event.game.Game
import gg.flyte.event.game.GameType
import org.bukkit.entity.Player
import java.util.*

abstract class MainGame(val type: GameType) : Game() {

    var state = MainGameState.COUNTDOWN

    val alive = mutableListOf<Player>()
    val points = mutableMapOf<UUID, Int>()

    abstract fun onPlayerJoin(player: Player)

}