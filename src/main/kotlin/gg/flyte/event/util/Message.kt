package gg.flyte.event.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

val CHRISTMAS_RED = TextColor.color(0xF85555)

val String.miniMessage: Component
    get() = MiniMessage.miniMessage().deserialize(this)

val Component.plainText: String
    get() = PlainTextComponentSerializer.plainText().serialize(this)