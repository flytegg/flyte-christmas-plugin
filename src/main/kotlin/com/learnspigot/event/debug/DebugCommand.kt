package com.learnspigot.event.debug

import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("debug")
@CommandPermission("admin")
object DebugCommand {

    // temp, no error checking
    @Subcommand("tp")
    fun tp(sender: CommandSender, world: String) {
        Bukkit.createWorld(WorldCreator("world"))
        (sender as Player).teleport(Bukkit.getWorld(world)!!.spawnLocation)
    }

}