package gg.flyte.event.util

import gg.flyte.event.ChristmasEvent
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class CooldownManager {

    private val itemCooldowns = mutableMapOf<UUID, MutableMap<String, Long>>()
    private val playerCooldowns = mutableMapOf<UUID, MutableMap<String, Long>>()

    fun enforceItemCooldown(item: ItemStack, player: Player, seconds: Double, message: String, abilityType: String) {
        val playerId = player.uniqueId
        val cooldownTimeMillis = (seconds * 1000).toLong()

        if (hasItemCooldown(playerId, abilityType)) {
            player.sendMessage(message)
        } else {
            setItemCooldown(playerId, abilityType, cooldownTimeMillis)

            object : BukkitRunnable() {
                override fun run() {
                    clearItemCooldown(playerId, abilityType)
                }
            }.runTaskLater(JavaPlugin.getPlugin(ChristmasEvent::class.java), (seconds * 20).toLong())
        }
    }

    fun enforcePlayerCooldown(player: Player, seconds: Double, message: String, abilityType: String) {
        val playerId = player.uniqueId
        val cooldownTimeMillis = (seconds * 1000).toLong()

        if (hasPlayerCooldown(playerId, abilityType)) {
            player.sendMessage(message)
        } else {
            setPlayerCooldown(playerId, abilityType, cooldownTimeMillis)

            object : BukkitRunnable() {
                override fun run() {
                    clearPlayerCooldown(playerId, abilityType)
                }
            }.runTaskLater(JavaPlugin.getPlugin(ChristmasEvent::class.java), (seconds * 20).toLong())
        }
    }

    fun getRemainingItemCooldown(player: Player, abilityType: String): Long {
        val playerId = player.uniqueId

        return itemCooldowns[playerId]?.getOrDefault(abilityType, 0) ?: 0
    }

    fun getRemainingPlayerCooldown(player: Player, abilityType: String): Long {
        val playerId = player.uniqueId

        return playerCooldowns[playerId]?.getOrDefault(abilityType, 0) ?: 0
    }

    private fun hasItemCooldown(playerId: UUID, abilityType: String): Boolean {
        return System.currentTimeMillis() < (itemCooldowns[playerId]?.get(abilityType) ?: 0)
    }

    private fun hasPlayerCooldown(playerId: UUID, abilityType: String): Boolean {
        return System.currentTimeMillis() < (playerCooldowns[playerId]?.get(abilityType) ?: 0)
    }

    private fun setItemCooldown(playerId: UUID, abilityType: String, cooldownMillis: Long) {
        itemCooldowns
            .getOrPut(playerId) { mutableMapOf() }
            .put(abilityType, System.currentTimeMillis() + cooldownMillis)
    }

    private fun setPlayerCooldown(playerId: UUID, abilityType: String, cooldownMillis: Long) {
        playerCooldowns
            .getOrPut(playerId) { mutableMapOf() }
            .put(abilityType, System.currentTimeMillis() + cooldownMillis)
    }

    private fun clearItemCooldown(playerId: UUID, abilityType: String) {
        itemCooldowns[playerId]?.remove(abilityType)
    }

    private fun clearPlayerCooldown(playerId: UUID, abilityType: String) {
        playerCooldowns[playerId]?.remove(abilityType)
    }
}
