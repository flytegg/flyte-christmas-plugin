package com.learnspigot.event.engine

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

abstract class Game {

    val tasks = mutableListOf<BukkitTask>()

    abstract fun start()

    abstract fun stop()

    abstract fun onPlayerJoin(player: Player)

    abstract fun onPlayerQuit(player: Player)

}