package com.learnspigot.event.lobby.game

object GameEngine {

    private val games = mutableMapOf<LobbyGameType, LobbyGame?>().apply {
        LobbyGameType.entries.forEach {
            this[it] = it.clazz.getDeclaredConstructor().newInstance() as LobbyGame
        }
    }

    fun startGame(type: LobbyGameType): LobbyGame? {
        return games[type] ?: null
    }

    fun endGame(type: LobbyGameType) {
        games[type] = null
    }

}