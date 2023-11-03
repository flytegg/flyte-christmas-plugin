package com.learnspigot.event.engine

import com.learnspigot.event.engine.GameEngine.game
import gg.flyte.twilight.extension.enumValue
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class GameCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendPlainMessage("Unknown command. Try /help for a list of commands")
            return false
        }

        if (args.isEmpty()) {
            sender.sendMessage(Component.text("No argument provided. Use either set, start or stop.").color(NamedTextColor.RED))
            return false
        }

        when (args[0].lowercase()) {
            "set" -> {
                if (args.size < 2) {
                    sender.sendMessage(Component.text("No game type provided. Use the tab complete.").color(NamedTextColor.RED))
                    return false
                }

                if (game != null) {
                    sender.sendMessage(Component.text("You can't do this while a game is ongoing.").color(NamedTextColor.RED))
                    return false
                }

                val type = enumValue<GameType>(args[1].uppercase())
                if (type == null) {
                    sender.sendMessage(Component.text("You provided an invalid game type. Use the tab complete.").color(NamedTextColor.RED))
                    return false
                }

                sender.sendMessage(Component.text("You have set the game type to $type.").color(NamedTextColor.GREEN))
                Bukkit.broadcast(Component.text("game changed to ").append(type.title))

                GameEngine.type = type
            }
            "start" -> {
                if (game != null) {
                    sender.sendMessage(Component.text("There is already a game ongoing.").color(NamedTextColor.RED))
                    return false
                }

                if (GameEngine.type == null) {
                    sender.sendMessage(Component.text("There is no game type set. Use /game set [game].").color(NamedTextColor.RED))
                    return false
                }

                sender.sendMessage(Component.text("You have started the game.").color(NamedTextColor.GREEN))

                GameEngine.start()
            }
            "stop" -> {
                if (game == null) {
                    sender.sendMessage(Component.text("There is no game to stop.").color(NamedTextColor.RED))
                    return false
                }

                sender.sendMessage(Component.text("You have stopped the game.").color(NamedTextColor.GREEN))

                GameEngine.stop()
            }
            else -> {
                sender.sendMessage(Component.text("Invalid usage. Use /game set/start/stop [game].").color(NamedTextColor.RED))
            }
        }
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        if (args.size == 1) {
            return StringUtil.copyPartialMatches(args[0], listOf("set","start","stop"), ArrayList());
        } else if (args.size == 2 && args[0].equals("set", ignoreCase = true)) {
            return StringUtil.copyPartialMatches(args[1], GameType.entries.map { it.name }, ArrayList());
        }
        return mutableListOf()
    }

}