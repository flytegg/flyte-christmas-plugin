package gg.flyte.event.game.main.type

import gg.flyte.event.game.GameType
import gg.flyte.event.game.main.TeamGame
import org.bukkit.entity.Player

class PresentSnatchGame : TeamGame(GameType.PRESENT_SNATCH) {

    override fun events() {
        // right click presents
        // disable damage of presents if theyre entities
        //
    }

    override fun start() {
        // spawn presents at mid/around the map
        // start timer
    }

    override fun stop() {
        // remove all presents if theyre entities
        // remeber to use MainGameEngine.stop() when stopping (cases: timer ends, too many players leave)
    }

    override fun onPlayerJoin(player: Player) {
        TODO("Not yet implemented")
    }

    override fun onPlayerQuit(player: Player) {
        TODO("Not yet implemented")
    }

}