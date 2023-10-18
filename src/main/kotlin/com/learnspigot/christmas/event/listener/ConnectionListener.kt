package com.learnspigot.christmas.event.listener

import com.learnspigot.christmas.event.ChristmasEvent
import com.learnspigot.christmas.event.util.npc.NPC
import com.learnspigot.christmas.event.visual.TablistManager
import gg.flyte.twilight.event.event
import gg.flyte.twilight.scheduler.delay
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent

object ConnectionListener {

    private val spawnLocation = Location(ChristmasEvent.WORLD, -22.5, 100.0, 4.5)

    init {
        event<PlayerJoinEvent> {
            joinMessage(null)

            player.apply {
                gameMode = GameMode.ADVENTURE
                foodLevel = 20
                health = 20.0
                //addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 10, false, false))
                teleport(spawnLocation)

                inventory.clear()
                //inventory.helmet = ItemStack(Material.CARVED_PUMPKIN)

                delay(20) {
                    NPC.sendAll(this@apply)
                    TablistManager.set(this@apply)
                }
            }
        }

        event<PlayerResourcePackStatusEvent> {

        }

        event<PlayerQuitEvent> {
            quitMessage(null)

            player.apply {
                TablistManager.remove(this)
            }
        }
    }

}