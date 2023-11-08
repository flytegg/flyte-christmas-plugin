package com.learnspigot.event.game.lobby

import com.learnspigot.event.game.Game
import org.bukkit.entity.Player

abstract class LobbyGame : Game() {

    abstract val player: Player

}