package com.learnspigot.event.engine

import com.learnspigot.event.engine.game.MusicalMinecartsGame
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
    val spectatorSpawn: MapLocation
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
    )

}