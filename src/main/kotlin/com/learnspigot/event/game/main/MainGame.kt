package com.learnspigot.event.game.main

import com.learnspigot.event.game.Game
import org.bukkit.entity.Player
import java.util.*

abstract class MainGame : Game() {

    var state = MainGameState.COUNTDOWN

    val points = mutableMapOf<UUID, Int>()

    abstract fun onPlayerJoin(player: Player)

}