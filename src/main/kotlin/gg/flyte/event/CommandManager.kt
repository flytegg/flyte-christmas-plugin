package gg.flyte.event

import gg.flyte.event.debug.DebugCommand
import gg.flyte.event.game.GameCommand
import gg.flyte.event.game.GameType
import gg.flyte.twilight.Twilight
import gg.flyte.twilight.extension.enumValue
import org.bukkit.Bukkit
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.exception.CommandErrorException
import revxrsal.commands.ktx.autoCompleter

object CommandManager {

    init {
        BukkitCommandHandler.create(Twilight.plugin).apply {
            registerAutoComplete()
            registerCommands()
            registerBrigadier()
        }
    }

    private fun BukkitCommandHandler.registerCommands() {
        register(
            GameCommand,
            DebugCommand
        )
    }

    private fun BukkitCommandHandler.registerAutoComplete() {
        registerValueResolver(GameType::class.java) {
            enumValue<GameType>(it.pop()) ?: throw CommandErrorException("Invalid game type!", it.pop())
        }

        registerValueResolver(DebugCommand.WorldName::class.java) { DebugCommand.WorldName(it.pop()) }

        autoCompleter {
            registerParameterSuggestions(GameType::class.java) { _, _, _ ->
                GameType.entries.map { it.name.uppercase() }
            }

            registerParameterSuggestions(DebugCommand.WorldName::class.java) { _, _, _ ->
                Bukkit.getWorlds().map { it.name }
            }
        }
    }

}