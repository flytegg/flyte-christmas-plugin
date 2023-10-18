package com.learnspigot.christmas.event.util.npc

import gg.flyte.twilight.event.event
import org.bukkit.Bukkit
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInteractEntityEvent

object NPCListener {

    init {
        event<PlayerInteractEntityEvent>(EventPriority.HIGH) {
            Bukkit.getPluginManager().callEvent(NPCClickEvent(player, NPC.npcs[rightClicked.uniqueId] ?: return@event))
        }
    }

}