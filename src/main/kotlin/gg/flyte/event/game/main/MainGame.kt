package gg.flyte.event.game.main

import gg.flyte.event.game.Game
import org.bukkit.entity.Player
import java.util.*

abstract class MainGame : Game() {

    var state = MainGameState.COUNTDOWN

    val points = mutableMapOf<UUID, Int>()

    abstract fun onPlayerJoin(player: Player)

}