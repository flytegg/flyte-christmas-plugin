package com.learnspigot.event.util.npc

import com.learnspigot.event.ChristmasEvent
import com.learnspigot.event.util.Skin
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.network.protocol.game.*
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.decoration.ArmorStand
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R1.CraftServer
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class NPC(
    location: Location,
    skin: Skin,
    followPlayer: Boolean,
    vararg holograms: String) : BukkitRunnable() {

    val serverPlayer: ServerPlayer
    val stands = mutableListOf<ArmorStand>()
    val interaction: Interaction

    private val tabID = createTabID()

    init {
        interaction = (location.world.spawnEntity(location, EntityType.INTERACTION) as Interaction).apply {
            interactionHeight = 3f
        }

        npcs[interaction.uniqueId] = this

        val gameProfile = GameProfile(UUID.randomUUID(), "NPC-$tabID")
        gameProfile.properties.put("textures", Property("textures", skin.texture, skin.signature))

        val server = (Bukkit.getServer() as CraftServer).server
        val world = (location.world as CraftWorld).handle

        serverPlayer = ServerPlayer(server, world, gameProfile)
        serverPlayer.moveTo(location.x, location.y, location.z, location.yaw, location.pitch)

        val headLocation = location.subtract(0.0, 0.45, 0.0)
        holograms.toList().reversed().forEach { text ->
            headLocation.add(0.0, 0.25, 0.0)
            stands.add(ArmorStand(world, headLocation.x, headLocation.y, headLocation.z).apply {
                moveTo(headLocation.x, headLocation.y, headLocation.z, headLocation.yaw, headLocation.pitch)
                customName = CraftChatMessage.fromStringOrNull(text)
                isInvulnerable = true
                isCustomNameVisible = true
                isInvisible = true
            })
        }

        if (followPlayer) {
            runTaskTimer(ChristmasEvent.INSTANCE, 5, 5)
        }
    }

    companion object {
        val npcs = mutableMapOf<UUID, NPC>()

        fun sendAll(player: Player) {
            npcs.values.forEach {
                it.spawn(player)
            }
        }

        fun createTabID(): Int {
            var randomID = (10_000..100_000).random()
            while (idTaken(randomID)) {
                randomID = (10_000..100_000).random()
            }
            return randomID
        }

        private fun idTaken(id: Int): Boolean {
            return npcs.values.any {
                it.tabID == id
            }
        }
    }

    override fun run() {
        var closest: Entity? = null
        var closestValue = Double.MAX_VALUE

        val craftPlayer = serverPlayer.bukkitEntity
        craftPlayer.getNearbyEntities(5.0, 5.0, 5.0).forEach { entity ->
            if (entity.type == EntityType.PLAYER) {
                val distance = craftPlayer.location.distance(entity.location)
                if (distance < closestValue) {
                    closest = entity
                    closestValue = distance
                }
            }
        }

        if (closest != null) {
            val location = craftPlayer.location.setDirection(closest!!.location.subtract(craftPlayer.location).toVector())
            val yaw = location.yaw
            val pitch = location.pitch
            (closest as CraftPlayer).handle.connection.apply {
                send(ClientboundMoveEntityPacket.Rot(serverPlayer.id, (yaw % 360.0 * 256 / 360).toInt().toByte(), (pitch % 360.0 * 256 / 360).toInt().toByte(), false))
                send(ClientboundRotateHeadPacket(serverPlayer, (yaw % 360.0 * 256 / 360).toInt().toByte()))
            }
        }
    }

    fun spawn(player: Player) {
        (player as CraftPlayer).handle.connection.apply {
            send(ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer))
            send(ClientboundAddEntityPacket(serverPlayer))
            send(ClientboundRotateHeadPacket(serverPlayer, (serverPlayer.bukkitYaw * 256 / 360).toInt().toByte()))

            val dataWatcher: SynchedEntityData = serverPlayer.entityData
            dataWatcher.set(EntityDataAccessor(17, EntityDataSerializers.BYTE), 127)
            send(ClientboundSetEntityDataPacket(serverPlayer.id, dataWatcher.packDirty()))

            stands.forEach { stand ->
                send(ClientboundAddEntityPacket(stand))
                send(ClientboundSetEntityDataPacket(stand.id, stand.entityData.packDirty()))
            }
        }
    }

}