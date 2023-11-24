package gg.flyte.event.game.lobby

import gg.flyte.event.ChristmasEvent
import gg.flyte.event.game.GameCategory
import gg.flyte.event.game.GameType
import gg.flyte.twilight.event.TwilightListener
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.extension.clearActionBar
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

object LobbyGameEngine {

    val games = mutableMapOf<Player, LobbyGame>()

    fun startGame(type: GameType, player: Player) {
        type.clazz.getDeclaredConstructor(Player::class.java).newInstance(player).apply {
            games[player] = this as LobbyGame
            events()
            start()
        }

        player.sendMessage(Component.text("game starting send info here"))
    }

    fun stopGame(player: Player, game: LobbyGame) {
        game.stop()
        games.remove(player)

        Bukkit.getOnlinePlayers().applyForEach {
            sendMessage("balls")
            clearTitle()
            clearActionBar()
            sendMessage("game ended")
        }

        game.let {
            it.stop()
            it.tasks.forEach(BukkitTask::cancel)
            it.events.forEach(TwilightListener::unregister)
        }
    }

}