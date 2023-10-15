package com.learnspigot.event.listener

import com.learnspigot.event.ChristmasEvent
import com.learnspigot.event.util.npc.NPC
import com.learnspigot.event.visual.TablistManager
import gg.flyte.twilight.scheduler.delay
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ConnectionListener : Listener {

    private val spawnLocation = Location(ChristmasEvent.WORLD, -22.5, 100.0, 4.5)

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage(null)

        e.player.apply {
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

    @EventHandler
    fun onPlayerResourcePackStatus(e: PlayerResourcePackStatusEvent) {

    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage(null)

        e.player.apply {
            TablistManager.remove(this)
        }
    }

}