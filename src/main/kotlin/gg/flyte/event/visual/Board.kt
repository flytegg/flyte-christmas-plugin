package gg.flyte.event.visual

import gg.flyte.event.util.CHRISTMAS_RED
import gg.flyte.event.util.Scoreboard
import net.kyori.adventure.text.Component.empty
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player

class Board(player: Player) : Scoreboard(player) {
    override fun title() = text("Christmas Event", CHRISTMAS_RED)

    override fun lines(player: Player): Array<TextComponent> = arrayOf(
        text("Test", CHRISTMAS_RED),
        empty(),
        text("flyte.gg"),
    )
}