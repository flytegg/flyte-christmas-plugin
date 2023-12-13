package gg.flyte.event.listener

import gg.flyte.event.ChristmasEvent
import gg.flyte.event.ChristmasEvent.Companion.LOBBY_SPAWN
import gg.flyte.event.game.main.MainGameEngine
import gg.flyte.event.util.CHRISTMAS_RED
import gg.flyte.event.util.npc.NPC
import gg.flyte.event.visual.TablistManager
import gg.flyte.event.visual.hud.impl.ActionBarImpl
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.RemoteFile
import gg.flyte.twilight.scheduler.delay
import net.kyori.adventure.text.Component.newline
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ConnectionListener {

    private val RED_CHRISTMAS_HAT = ItemStack(Material.LEATHER).apply {
        itemMeta = itemMeta.apply {
            displayName(text("Christmas Hat", NamedTextColor.RED))
            setCustomModelData(1)
        }
    }

    private val BLUE_CHRISTMAS_HAT = ItemStack(Material.LEATHER).apply {
        itemMeta = itemMeta.apply {
            displayName(text("Christmas Hat", NamedTextColor.BLUE))
            setCustomModelData(2)
        }
    }

    private val GREEN_CHRISTMAS_HAT = ItemStack(Material.LEATHER).apply {
        itemMeta = itemMeta.apply {
            displayName(text("Christmas Hat", NamedTextColor.GREEN))
            setCustomModelData(3)
        }
    }

    val playerLimit = 100

    init {
        event<PlayerLoginEvent> {
            if (player.canBypassPlayerLimit()) return@event
            if (ChristmasEvent.SERVER.onlinePlayers.size < playerLimit) return@event
            disallow(PlayerLoginEvent.Result.KICK_FULL, text().append(
                text("Sorry, the event is currently full.", CHRISTMAS_RED),
                newline(),
                newline(),
                text("You can still join the fun over at", NamedTextColor.WHITE),
                newline(),
                text("twitch.tv/flytelabs", CHRISTMAS_RED)
            ).build())
        }

        event<PlayerJoinEvent> {
            joinMessage(null)

            player.apply {
                // While in dev, get resource pack every join as hash could have changed. When finished making changes move back to class initialisation to prevent getting every join
                RemoteFile("https://github.com/flytegg/ls-christmas-rp/releases/latest/download/RP.zip").apply {
                    println("resource pack hash = $hash")
                    setResourcePack(url, hash, true)
                }

                MainGameEngine.onPlayerJoin(this)
                ActionBarImpl.register(this)

                gameMode = GameMode.ADVENTURE
                foodLevel = 20
                teleport(LOBBY_SPAWN)
                getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 20.0
                health = 20.0

                inventory.clear()
                inventory.helmet = RED_CHRISTMAS_HAT

                delay(20) {
                    NPC.sendAll(this@apply)
                    TablistManager.set(this@apply)
                }
            }
        }

        event<PlayerResourcePackStatusEvent> {
            if (status == PlayerResourcePackStatusEvent.Status.ACCEPTED || status == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) return@event
            player.kick(
                text("Failed to load resource pack, please try again.", NamedTextColor.RED),
                PlayerKickEvent.Cause.RESOURCE_PACK_REJECTION
            )
        }

        event<PlayerQuitEvent> {
            quitMessage(null)

            player.apply {
                MainGameEngine.onPlayerQuit(this)
                ActionBarImpl.unregister(this)
                TablistManager.remove(this)
            }
        }

        event<InventoryClickEvent> {
            if (clickedInventory !is PlayerInventory) return@event
            isCancelled = slotType == InventoryType.SlotType.ARMOR
        }
    }

    private fun Player.canBypassPlayerLimit() = isOp || isWhitelisted

}