package gg.flyte.event.game.main

import gg.flyte.event.game.Game
import gg.flyte.event.game.GameType
import org.bukkit.entity.Player
import java.util.*

abstract class MainGame(type: GameType) : Game() {

    lateinit var type: GameType
    var state = MainGameState.COUNTDOWN

    val alive = mutableListOf<Player>()
    val points = mutableMapOf<UUID, Int>()

    abstract fun onPlayerJoin(player: Player)

}