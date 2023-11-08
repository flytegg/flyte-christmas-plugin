package com.learnspigot.event.game

import com.learnspigot.event.game.main.MainGameEngine
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.DefaultFor
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.ktx.commandError

@Command("game")
@CommandPermission("admin")
object GameCommand {

    @DefaultFor
    fun game() {
        commandError("Invalid usage. Use /game set/start/stop [game].")
    }

    @Subcommand("set")
    fun set(sender: CommandSender, game: GameType) {
        if (MainGameEngine.game != null) commandError("Cannot change game whilst current ongoing.")

        MainGameEngine.type = game
        sender.sendMessage(Component.text("You have set the game type to $game.").color(NamedTextColor.GREEN))
        Bukkit.broadcast(Component.text("game changed to ").append(game.title))
    }

    @Subcommand("start")
    fun start(sender: CommandSender) {
        if (MainGameEngine.game != null) commandError("There is already a game ongoing.")
        if (MainGameEngine.type == null) commandError("There is no game type set. Use /game set [game].")

        sender.sendMessage(Component.text("You have started the game.").color(NamedTextColor.GREEN))
        MainGameEngine.start()
    }

    @Subcommand("stop")
    fun stop(sender: CommandSender) {
        if (MainGameEngine.game == null && MainGameEngine.countdownTask == null) commandError("There is no game to stop.")

        sender.sendMessage(Component.text("You have stopped the game.").color(NamedTextColor.GREEN))
        MainGameEngine.stop()
    }

}