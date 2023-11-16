package com.learnspigot.event.debug

import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.ktx.commandError

@Command("debug")
@CommandPermission("admin")
object DebugCommand {
    init {
        Bukkit.createWorld(WorldCreator("build"))
        Bukkit.createWorld(WorldCreator("tree"))
    }

    @Subcommand("tp")
    fun tp(player: Player, worldName: WorldName) {
        val world = Bukkit.getWorld(worldName.value) ?: commandError("World ${worldName.value} does not exist.")
        player.apply {
            teleport(world.spawnLocation)
            sendMessage("Teleported to ${world.name}")
        }
    }

    data class WorldName(val value: String)

}