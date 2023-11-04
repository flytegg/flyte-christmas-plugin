package com.learnspigot.event.engine

import gg.flyte.twilight.event.TwilightListener
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

abstract class Game {

    var state = GameState.COUNTDOWN

    val tasks = mutableListOf<BukkitTask>()
    val events = mutableListOf<TwilightListener>()

    val points = mutableMapOf<UUID, Int>()

    abstract fun events()

    abstract fun start()

    abstract fun stop()

    abstract fun onPlayerJoin(player: Player)

    abstract fun onPlayerQuit(player: Player)

}