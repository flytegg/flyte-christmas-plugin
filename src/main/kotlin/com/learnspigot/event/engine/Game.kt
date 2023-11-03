package com.learnspigot.event.engine

import org.bukkit.entity.Player

abstract class Game {

    abstract fun start()

    abstract fun stop()

    abstract fun onPlayerJoin(player: Player)

    abstract fun onPlayerQuit(player: Player)

}