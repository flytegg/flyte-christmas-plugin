package com.learnspigot.event.engine

import com.learnspigot.event.engine.game.MusicalMinecartsGame
import com.learnspigot.event.engine.game.SledRacingGame
import com.learnspigot.event.util.MapLocation
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.util.BoundingBox

enum class GameType(
    val title: Component,
    val description: Component,
    val gameMode: GameMode,
    val clazz: Class<out Game>,
    val region: BoundingBox,
    val spawns: List<MapLocation>,
    val spectatorSpawn: MapLocation?
) {

    MUSICAL_MINECARTS(
        Component.text("Musical Minecarts"),
        Component.text("test game"),
        GameMode.ADVENTURE,
        MusicalMinecartsGame::class.java,
        BoundingBox.of(
            MapLocation(-145, 86, 69),
            MapLocation(-135, 79, 60)
        ),
        listOf(
            MapLocation(-138, 80, 66)
        ),
        MapLocation(-149, 80, 65, -90, 0)
    ),

    SLED_RACING(
        Component.text("Sled Racing"),
        Component.text("test game2"),
        GameMode.ADVENTURE,
        SledRacingGame::class.java,
        BoundingBox.of(
            MapLocation(-133, 83, 95),
            MapLocation(-174, 79, 103)
        ),
        listOf(
            MapLocation(-133, 80, 100, 90, 0)
        ),
        MapLocation(-151, 80, 92)
    )



}