package com.learnspigot.event.game

import gg.flyte.twilight.event.TwilightListener
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

abstract class Game {

    val tasks = mutableListOf<BukkitTask>()
    val events = mutableListOf<TwilightListener>()

    abstract fun events()

    abstract fun start()

    abstract fun stop()

    abstract fun onPlayerQuit(player: Player)

}