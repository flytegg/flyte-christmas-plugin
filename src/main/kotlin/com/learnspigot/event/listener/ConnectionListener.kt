package com.learnspigot.event.listener

import com.learnspigot.event.Event
import com.learnspigot.event.util.npc.NPC
import com.learnspigot.event.visual.TablistManager
import gg.flyte.twilight.scheduler.delay
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ConnectionListener : Listener {

    private val LOBBY_SPAWN = Location(Bukkit.getWorld("world"), -22.5, 100.0, 4.5)

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage(null)

        e.player.apply {
            gameMode = GameMode.ADVENTURE
            addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 10, false, false))
            teleport(LOBBY_SPAWN)

            inventory.clear()
     //       inventory.helmet = ItemStack(Material.CARVED_PUMPKIN)

            delay(20) {
                NPC.sendAll(this@apply)
                TablistManager.set(this@apply)
            }//
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