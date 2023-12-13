package gg.flyte.event.game.main.type

import gg.flyte.event.game.main.MainGame
import gg.flyte.event.game.main.MainGameEngine
import gg.flyte.event.util.NBSSongType
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer
import gg.flyte.event.game.GameType
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.scheduler.repeat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffectType

class BlockPartyGame : MainGame(GameType.MUSICAL_MINECARTS) {// CHnage to block party

    private val squares = listOf<Location>()

    private val MATERIALS = mapOf(
        Material.WHITE_CONCRETE to NamedTextColor.WHITE,
        Material.ORANGE_CONCRETE to NamedTextColor.GOLD,
        Material.MAGENTA_CONCRETE to NamedTextColor.DARK_PURPLE,
        Material.LIGHT_BLUE_CONCRETE to NamedTextColor.AQUA,
        Material.YELLOW_CONCRETE to NamedTextColor.YELLOW,
        Material.LIME_CONCRETE to NamedTextColor.GREEN,
        Material.PINK_CONCRETE to NamedTextColor.LIGHT_PURPLE,
        Material.GRAY_CONCRETE to NamedTextColor.DARK_GRAY,
        Material.LIGHT_GRAY_CONCRETE to NamedTextColor.GRAY,
        Material.CYAN_CONCRETE to NamedTextColor.DARK_AQUA,
        Material.PURPLE_CONCRETE to NamedTextColor.DARK_PURPLE,
        Material.BLUE_CONCRETE to NamedTextColor.BLUE,
        Material.BROWN_CONCRETE to NamedTextColor.GOLD,
        Material.GREEN_CONCRETE to NamedTextColor.GREEN,
        Material.RED_CONCRETE to NamedTextColor.RED,
        Material.BLACK_CONCRETE to NamedTextColor.BLACK
    )

    private lateinit var music: RadioSongPlayer

    init {


    }

    override fun events() {
        events += event<PlayerDropItemEvent> {
            isCancelled = true
        }

        events += event<InventoryClickEvent> {
            isCancelled = true
        }

        events += event<PlayerInteractEvent> {

        }
    }

    override fun start() {
        tasks += repeat(4, 4) {
            gameLoop()
        }

        val song = NBSSongType.entries.random()
        music = RadioSongPlayer(song.song)
        music.repeatMode = RepeatMode.ALL
        Bukkit.getOnlinePlayers().forEach(music::addPlayer)

        Bukkit.broadcast(
            Component.text().append(
                Component.newline(),
                Component.text("♫ ").color(NamedTextColor.RED), Component.text("Now playing ${song.title}..."),
                Component.newline(),
            ).build()
        )

        newRound()
    }

    private val RESPAWN_Y = 78

    private fun gameLoop() {
        val iterator = alive.iterator()
        if (iterator.hasNext()) {
            val player = iterator.next()
            if (player.location.y <= RESPAWN_Y) {
                iterator.remove()

                player.apply {

                }
            }
        }

        if (alive.size <= 1) {
            music.destroy()
            MainGameEngine.stop()
        }
    }

    private fun newRound() {

    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun onPlayerJoin(player: Player) {
        TODO("Not yet implemented")
    }

    override fun onPlayerQuit(player: Player) {
        TODO("Not yet implemented")
    }

}