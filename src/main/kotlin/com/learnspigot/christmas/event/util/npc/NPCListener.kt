package com.learnspigot.christmas.event.util.npc

import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.world.ChunkLoadEvent

object NPCListener : Listener {

    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        Bukkit.getPluginManager().callEvent(NPCClickEvent(e.player, NPC.npcs[e.rightClicked.uniqueId] ?: return))
    }

}