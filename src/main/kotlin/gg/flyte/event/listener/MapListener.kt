package gg.flyte.event.listener

import gg.flyte.twilight.event.event
import org.bukkit.entity.EntityType
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

object MapListener {

    init {
        event<BlockBreakEvent>(priority = EventPriority.LOWEST) {
            isCancelled = !player.isOp
        }

        event<BlockPlaceEvent>(priority = EventPriority.LOWEST) {
            isCancelled = !player.isOp
        }

        event<EntityDamageEvent>(priority = EventPriority.LOWEST) {
            isCancelled = entityType == EntityType.PLAYER
        }

        event<FoodLevelChangeEvent> {
            isCancelled = true
        }
    }

}