package com.learnspigot.event.game.lobby

import com.learnspigot.event.game.GameCategory
import com.learnspigot.event.game.GameType

object LobbyGameEngine {

    private val games = mutableMapOf<GameType, LobbyGame?>().apply {
        GameType.entries.filter { it.category == GameCategory.LOBBY }.forEach {
            this[it] = it.clazz.getDeclaredConstructor().newInstance() as LobbyGame
        }
    }

    fun startGame(type: GameType): LobbyGame? {
        return games[type]
    }

    fun endGame(type: GameType) {
        games[type] = null
    }

}