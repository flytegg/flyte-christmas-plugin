package gg.flyte.event.game.main.type

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer
import gg.flyte.event.game.GameType
import gg.flyte.event.game.main.MainGame
import gg.flyte.event.game.main.MainGameEngine
import gg.flyte.event.util.NBSSongType
import gg.flyte.twilight.event.event
import gg.flyte.twilight.scheduler.delay
import gg.flyte.twilight.time.TimeUnit
import net.kyori.adventure.text.Component.newline
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import kotlin.random.Random

class PassTheParcelGame : MainGame(GameType.PASS_THE_PARCEL) {
    private lateinit var music: RadioSongPlayer

    override fun events() {
        events += event<InventoryOpenEvent> {
            if (inventory != player.inventory) return@event
            isCancelled = true
            handleParcelLand()
        }
    }

    override fun start() {
        val song = NBSSongType.entries.random()
        music = RadioSongPlayer(song.song)
        music.repeatMode = RepeatMode.ALL
        Bukkit.getOnlinePlayers().forEach(music::addPlayer)

        Bukkit.broadcast(
            text().append(
                newline(),
                text("♫ ").color(NamedTextColor.RED), text("Now playing ${song.title}..."),
                newline(),
            ).build()
        )

        music.isPlaying = true

        // Give to first player
        nextPlayer()
    }

    private var parcelHolder: Player? = null

    private fun nextPlayer() {
        parcelHolder = parcelHolder?.let {
            alive.elementAtOrElse(alive.indexOf(it) + 1) { alive.first() }
        } ?: alive.first()

        val player = parcelHolder ?: throw IllegalStateException()

        Bukkit.broadcast(text("${player.name} HAS THE PARCEL"))

        // If haven't opened after 3 seconds, trigger opening parcel automatically
        tasks += delay(3, TimeUnit.SECONDS) {
            if (parcelHolder == null) return@delay
            if (player.uniqueId == parcelHolder!!.uniqueId) handleParcelLand()
        }
    }

    private fun handleParcelLand() {
        val parcelHolder = parcelHolder ?: return nextPlayer()

        // 40% chance of music stopping
        if (Random.nextInt(100) >= 60) {
            music.isPlaying = false
            Bukkit.broadcast(text("THE PARCEL LANDED ON ${parcelHolder.name}"))
            tasks += delay(2, TimeUnit.SECONDS) { openParcel() }
        }

        // Pass to next player
        tasks += delay(3, TimeUnit.SECONDS) {
            nextPlayer()
            music.isPlaying = true
        }
    }

    private fun openParcel() {
        val parcelHolder = parcelHolder ?: return nextPlayer()

        // 30% chance of blowing up
        if (Random.nextInt(100) >= 70) {
            handleElimination(parcelHolder)
            Bukkit.broadcast(text("${parcelHolder.name} went BOOM"))
        } else {
            points += parcelHolder.uniqueId to points.getOrDefault(parcelHolder.uniqueId, 0) + 1
            Bukkit.broadcast(text("${parcelHolder.name} didn't die!"))
        }
    }

    private fun handleElimination(player: Player) {
        alive -= player
        if (alive.size <= 1) MainGameEngine.stop()
    }

    override fun stop() {
        music.destroy()
    }

    override fun onPlayerJoin(player: Player) {

    }

    override fun onPlayerQuit(player: Player) {

    }
}