package com.learnspigot.event.listener

import com.learnspigot.event.ChristmasEvent.Companion.LOBBY_SPAWN
import com.learnspigot.event.ChristmasEvent.Companion.RED_CHRISTMAS_HAT
import com.learnspigot.event.ChristmasEvent.Companion.RESOURCE_PACK_URL
import com.learnspigot.event.engine.GameEngine
import com.learnspigot.event.util.npc.NPC
import com.learnspigot.event.visual.TablistManager
import gg.flyte.twilight.event.event
import gg.flyte.twilight.scheduler.delay
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ConnectionListener {

    init {
        event<PlayerJoinEvent> {
            joinMessage(null)

            player.apply {
                setResourcePack(RESOURCE_PACK_URL, null, true)

                GameEngine.onPlayerJoin(this)

                gameMode = GameMode.ADVENTURE
                foodLevel = 20
                health = 20.0
                addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 10, false, false))
                teleport(LOBBY_SPAWN)

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
                GameEngine.onPlayerQuit(this)
                TablistManager.remove(this)
            }
        }

        event<InventoryClickEvent> {
            if (clickedInventory !is PlayerInventory) return@event
            isCancelled = slotType == InventoryType.SlotType.ARMOR
        }
    }

}