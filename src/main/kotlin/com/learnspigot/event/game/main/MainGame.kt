package com.learnspigot.event.game.main

import com.learnspigot.event.game.Game
import com.learnspigot.event.game.GameState
import gg.flyte.twilight.event.TwilightListener
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

abstract class MainGame : Game() {

    val points = mutableMapOf<UUID, Int>()

    abstract fun onPlayerJoin(player: Player)

}