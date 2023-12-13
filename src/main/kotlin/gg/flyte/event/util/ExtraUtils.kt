package gg.flyte.event.util

import gg.flyte.event.ChristmasEvent
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

class ExtraUtils {

    fun isPathObstructed(start: Location, end: Location): Int {
        if (start.world != null && end.world != null && start.world == end.world) {
            val direction = end.toVector().subtract(start.toVector())
            val totalDistance = Math.floor(direction.length())
            direction.multiply(1.0 / direction.length())

            for (distance in 0..totalDistance.toInt()) {
                var currentDirection = end.toVector().subtract(start.toVector())
                currentDirection.multiply(1.0 / currentDirection.length())
                val currentBlock = start.world.getBlockAt(start.toVector().add(currentDirection.multiply(distance.toDouble())).toLocation(start.world))
                if (currentBlock.type.isSolid) {
                    return distance
                }
            }
            return -1
        } else {
            return 0
        }
    }

    fun safelyHeal(entity: LivingEntity, amount: Int) {
        val maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?: return
        if (entity.health + amount.toDouble() >= maxHealth) {
            entity.health = maxHealth
        } else {
            entity.health += amount.toDouble()
        }
    }


    fun safeTeleport(entity: Entity, initialLocation: Location, targetLocation: Location) {
        if (initialLocation.world != null && targetLocation.world != null && initialLocation.world == targetLocation.world) {
            val pathObstruction = isPathObstructed(initialLocation, targetLocation)
            if (pathObstruction == -1) {
                entity.teleport(targetLocation)
            } else {
                val offsetLocation = initialLocation.clone().add(
                    entity.location.add(0.0, entity.height - 1.0, 0.0).direction.multiply(pathObstruction)
                )
                val blockAtOffset = initialLocation.world.getBlockAt(offsetLocation)
                if (blockAtOffset.type.isSolid) {
                    entity.teleport(offsetLocation.add(0.0, 1.0, 0.0))
                } else {
                    entity.teleport(offsetLocation, *emptyArray())
                }
            }
        }
    }
    fun dyeArmor(itemStack: ItemStack, color: Color): ItemStack {
        if (itemStack.itemMeta !is LeatherArmorMeta) {
            return itemStack
        } else {
            val var2 = itemStack.getItemMeta() as LeatherArmorMeta
            var2.setColor(color)
            itemStack.setItemMeta(var2)
            return itemStack


        }
    }

    fun givePlayerItemSafely(player: Player, item: ItemStack) {
        val remainingItems = player.inventory.addItem(item)
        remainingItems.values.filterNotNull().filter { it.type != Material.AIR }.forEach { remainingItem ->
            val droppedItem = player.world.dropItemNaturally(player.location, remainingItem)
            droppedItem.velocity = player.location.direction.multiply(0.1)
        }
    }




    fun tagEntity(var0: Entity?, var1: String, var2: String) {
        val var3 = NamespacedKey(ChristmasEvent.INSTANCE, var2)
        var0?.persistentDataContainer?.set(var3, StoredString(), var1)
    }

    fun getEntityTag(var0: Entity?, var1: String): String? {
        val var2 = NamespacedKey(ChristmasEvent.INSTANCE, var1)
        return if (var0 == null) {
            null
        } else {
            var var3 = var0.persistentDataContainer.get(var2, StoredString()) as String?
            if (var3 == null) {
                var3 = ""
            }
            var3
        }
    }
    }