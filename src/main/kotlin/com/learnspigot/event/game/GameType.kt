package com.learnspigot.event.game

import com.learnspigot.event.game.lobby.type.PresentHuntGame
import com.learnspigot.event.game.main.MainGame
import com.learnspigot.event.game.main.type.KingOfTheHillGame
import com.learnspigot.event.game.main.type.MusicalMinecartsGame
import com.learnspigot.event.game.main.type.SledRacingGame
import com.learnspigot.event.util.MapLocation
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.util.BoundingBox
import javax.naming.ldap.PagedResultsResponseControl

enum class GameCategory {
    LOBBY,
    MAIN
}

enum class GameType(
    val category: GameCategory,
    val title: Component,
    val description: Component,
    val gameMode: GameMode,
    val clazz: Class<out Game>,
    val region: BoundingBox,
    val spawns: List<MapLocation>,
    val spectatorSpawn: MapLocation?
) {

    // MAIN GAMES

    MUSICAL_MINECARTS(
        GameCategory.MAIN,
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
        GameCategory.MAIN,
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
    ),

    KING_OF_THE_HILL(
        GameCategory.MAIN,
        Component.text("King Of The Hill"),
        Component.text("test game3"),
        GameMode.ADVENTURE,
        KingOfTheHillGame::class.java,
        BoundingBox.of(
            MapLocation(-194, 84, 67),
            MapLocation(-189, 82, 63)
        ),
        listOf(
            MapLocation(-190, 82, 65, 90, 0)
        ),
        MapLocation(-180, 80, 64, 90, 0)
    ),


    // LOBBY GAMES

    PRESENT_HUNT(
        GameCategory.LOBBY,
        Component.text("Presnt Hunt"),
        Component.text("adwdwawd"),
        GameMode.ADVENTURE,
        PresentHuntGame::class.java,
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