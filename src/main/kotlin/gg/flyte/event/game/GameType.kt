package gg.flyte.event.game

import gg.flyte.event.game.lobby.type.PresentHuntGame
import gg.flyte.event.game.main.type.*
import gg.flyte.event.util.MapLocation
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.util.BoundingBox

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
    val spawns: List<List<MapLocation>>, // Teams -> Spawns
    val spectatorSpawn: MapLocation?
) {

    // MAIN GAMES

    BLOCK_PARTY(
        GameCategory.MAIN,
        Component.text("Block Party"),
        Component.text("test game"),
        GameMode.ADVENTURE,
        BlockPartyGame::class.java,
        BoundingBox.of(
            MapLocation(633, 126, 783),
            MapLocation(599, 126, 817)
        ),
        listOf(
            listOf(
                MapLocation(616, 111, 800)
            )
        ),
        MapLocation(635, 112, 828, 145, 0)
    ),

    MUSICAL_MINECARTS(
        GameCategory.MAIN,
        Component.text("Musical Minecarts"),
        Component.text("test game"),
        GameMode.ADVENTURE,
        MusicalMinecartsGame::class.java,
        BoundingBox.of(
            MapLocation(633, 126, 783),
            MapLocation(599, 126, 817)
        ),
        listOf(
            listOf(
                MapLocation(616, 111, 800)
            )
        ),
        MapLocation(635, 112, 828, 145, 0)
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
            listOf(
                MapLocation(-133, 80, 100, 90, 0)
            )
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
            MapLocation(828, 101, 623),
            MapLocation(832, 104, 627)
        ),
        listOf(
            listOf(
                MapLocation(811, 98, 604, -45, -5),
                MapLocation(831, 100, 600, 3, 0),
                MapLocation(845, 103, 606, 40, 7),
                MapLocation(855, 100, 622, 60, 5),
                MapLocation(855, 101, 640, 135, 3),
                MapLocation(845, 101, 656, 151, 3),
                MapLocation(814, 100, 644, -135, 2),
                MapLocation(803, 96, 624, -90, -7),
            )
        ),
        MapLocation(731, 142, 528, -45, 15)
    ),

    PRESENT_SNATCH(
        GameCategory.MAIN,
        Component.text("Present Snatch"),
        Component.text("awooga"),
        GameMode.ADVENTURE,
        PresentSnatchGame::class.java,
        BoundingBox.of(
            MapLocation(0, 0, 0),
            MapLocation(0, 0, 0),
        ),
        listOf(
            listOf(
                MapLocation(0, 0, 0),
            ),
            listOf(
                MapLocation(0, 0, 0),
            ),
            listOf(
                MapLocation(0, 0, 0),
            ),
            listOf(
                MapLocation(0, 0, 0),
            ),
        ),
        MapLocation(0, 0, 0)
    ),


    // LOBBY GAMES

    PRESENT_HUNT(
        GameCategory.LOBBY,
        Component.text("Presnt Hunt"),
        Component.text("adwdwawd"),
        GameMode.ADVENTURE,
        PresentHuntGame::class.java,
        BoundingBox.of(
            MapLocation(413, 300, 624),
            MapLocation(791, 35, 311)
        ),
        listOf(
            listOf(
                MapLocation(611, 94, 502, 45, -3)
            ),
        ),
        null
    )

}