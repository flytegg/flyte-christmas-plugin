package gg.flyte.event.game.main

import gg.flyte.event.game.GameType
import gg.flyte.event.game.TeamType
import org.bukkit.entity.Player

abstract class TeamGame(type: GameType) : MainGame(type) {

    lateinit var teams: MutableMap<TeamType, MutableList<Player>>

}