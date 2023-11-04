package com.learnspigot.event.engine

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitTask

abstract class Game : Listener {

    val tasks = mutableListOf<BukkitTask>()

    abstract fun events()

    abstract fun start()

    abstract fun stop()

    abstract fun onPlayerJoin(player: Player)

    abstract fun onPlayerQuit(player: Player)

}