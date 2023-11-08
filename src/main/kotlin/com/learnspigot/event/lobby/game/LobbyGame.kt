package com.learnspigot.event.lobby.game

import com.learnspigot.event.engine.GameState
import gg.flyte.twilight.event.TwilightListener
import gg.flyte.twilight.extension.applyForEach
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

abstract class LobbyGame {

    var state = GameState.COUNTDOWN

    val tasks = mutableListOf<BukkitTask>()
    val events = mutableListOf<TwilightListener>()

    abstract fun events()

    abstract fun start()

    abstract fun stop()

    abstract fun onPlayerQuit(player: Player)

}