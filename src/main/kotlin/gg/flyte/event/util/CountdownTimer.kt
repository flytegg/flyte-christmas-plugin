package gg.flyte.event.util

import org.bukkit.plugin.Plugin
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class CountdownTimer(private val plugin: Plugin) {
    private val countdowns: MutableMap<UUID, MutableMap<ItemStack, Long>> = HashMap()
    private val onFinishCallbacks: MutableMap<UUID, Runnable> = HashMap()

    fun startCountdown(player: Player, itemStack: ItemStack, seconds: Int, onFinish: Runnable) {
        val playerId = player.uniqueId
        countdowns.computeIfAbsent(playerId) { HashMap() }

        // Store the countdown end time for the item
        val endTime = System.currentTimeMillis() + (seconds * 1000)
        countdowns[playerId]!![itemStack] = endTime

        // Store the onFinish callback for this countdown
        onFinishCallbacks[playerId] = onFinish

        // Schedule a task to remove the countdown after the specified time
        object : BukkitRunnable() {
            override fun run() {
                stopCountdown(player, itemStack)
                // Execute the onFinish callback when the countdown expires
                onFinish.run()
            }
        }.runTaskLater(plugin, seconds * 20.toLong()) // Convert seconds to ticks (20 ticks per second)
    }

    fun isCountingDown(player: Player, itemStack: ItemStack): Boolean {
        val playerId = player.uniqueId
        if (countdowns.containsKey(playerId)) {
            val itemCooldowns = countdowns[playerId]
            if (itemCooldowns!!.containsKey(itemStack)) {
                val endTime = itemCooldowns[itemStack]!!
                return System.currentTimeMillis() < endTime
            }
        }
        return false
    }

    fun stopCountdown(player: Player, itemStack: ItemStack) {
        val playerId = player.uniqueId
        if (countdowns.containsKey(playerId)) {
            val itemCooldowns = countdowns[playerId]
            itemCooldowns!!.remove(itemStack)
            if (itemCooldowns.isEmpty()) {
                countdowns.remove(playerId)
                // Remove the onFinish callback when all countdowns for the player are cleared
                onFinishCallbacks.remove(playerId)
            }
        }
    }
}
