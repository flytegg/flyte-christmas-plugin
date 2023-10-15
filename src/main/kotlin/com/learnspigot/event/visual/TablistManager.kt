package com.learnspigot.event.visual

import com.learnspigot.event.Event
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import gg.flyte.twilight.string.smallText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_20_R1.CraftServer
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import java.util.*

object TablistManager {

    private val TAB_FOOTER = " \n" + ChatColor.GRAY + "CHANGE THIIS\n" + ChatColor.AQUA + "CAHNGE THIS\n "

    private val fakePlayers = mutableListOf<ServerPlayer>().apply {
        val server = (Bukkit.getServer() as CraftServer).server
        val world = (Event.CAMPUS as CraftWorld).handle
        val property = Property(
            "textures",
            "ewogICJ0aW1lc3RhbXAiIDogMTYwNzcxNjUwMzI2OCwKICAicHJvZmlsZUlkIiA6ICJlYjIwYWVkYTRiYTM0NzVmOTczZGNmZjkzNTE2OGZhYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa3lGYWxsZWVlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2IxYmI1NzdkZjg3OGU4NzY1YzhjMDZmZWVlY2Y5ODA3OTRlZWE5ODI4MDFjMjc5NWI4MjY4ZjA5MzI2MGEwZTAiCiAgICB9CiAgfQp9",
            "s/TIW86bb1jKqnGqgaGMakBqVCQRgpsqM8MKcfULp4BRcXrMNkax1jKA121cEG1SjoPWGjH1cqMdH9KKqnHFbhE1yFDhMFKuBKRRJyGyV+DRdtbz0fDV5mGABoDHcY2u4PcQJh6pova/+IaaDn66F8xKduMET6BHuvhjrGFbX9r+ZFVUQS1EuF51EWblKvOUkDCMH6pBzbdqeXRhQfbbm6KpRttVxnNKbsxiRdDEvJ8j67JrAy6p2BTXr/gFW/BF6ktvFqLL9OQzH/cW/TScZYglvTUb1GnztxPn/PbYJlCMKL4QTEAJs1Ti7l4Uj5do4/cK/AXxsuvEqTMArvdBrwv6+AgCvFnh7PPTIRHMzTHyLmSHr1ikKtjloQgnkQVLO5gFEPnuExzAbvTZYL+F2NJN3OTQZ0KBWriIgS2futweFehKuhMoksLo056yb7coJpeaX/BAAcloRiC3IilozepdIYVnCxAw4MgW5Uy7gSoE7xBDIg2e0+24kdjA0yEpfrm9SBa9xhcpzVtH+g7zlKil4gEQhptNL7kz9iSMEMuNNFI8dLZs1jnTiscti5D/gp3BqscCX8wv4Nx2uRaQgrclxbZ4A/s81jAlbPhw5TdTZwFojFLCDDoBtpNA9zj4/YjcfXnN/3vBKmzL/2k8JPs+GAUG/5tFARpZ0yhWDgk=")

        repeat(84) {
            val gameProfile = GameProfile(UUID.randomUUID(), "                ")
            gameProfile.properties.put("textures", property)
            add(ServerPlayer(server, world, gameProfile))
        }

//        ChatListener.emojis.keys.forEach {
//            add(ServerPlayer(server, world, GameProfile(UUID.randomUUID(), it)))
//        }
    }

    fun set(player: Player) {
        updateHeaderFooter()
        sendTablist(player)
        setNametags(player)
        addTag(player)
    }

    fun remove(player: Player) {
        updateHeaderFooter()

        val name = player.name
        Bukkit.getOnlinePlayers().forEach {
            it.scoreboard.getEntryTeam(name)!!.removeEntry(name)
        }
    }

    private fun updateHeaderFooter() {
        Bukkit.getOnlinePlayers().forEach {
            it.setPlayerListHeaderFooter(" \nLOGOUNICODE\n \n" + ChatColor.BLUE + Bukkit.getOnlinePlayers().size + " ONLINE\n ", TAB_FOOTER)
        }
    }

    private fun sendTablist(player: Player) {
        fakePlayers.forEach {
            (player as CraftPlayer).handle.connection.send(ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, it))
            player.handle.connection.send(ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, it))
        }
    }

    private fun setNametags(player: Player) {
        val board = Bukkit.getScoreboardManager().newScoreboard.apply { player.scoreboard = this }

        board.registerNewTeam("op").apply {
            setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
            prefix(Component.text("Staff ".smallText()).color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
            color(NamedTextColor.RED)
        }

        board.registerNewTeam("regular").apply {
            setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
            color(NamedTextColor.GRAY)
        }

        Bukkit.getOnlinePlayers().forEach { target ->
            board.getTeam(if (target.isOp) "op" else "regular")!!.addEntry(target.name)
        }

        board.registerNewTeam("yFake").apply {
            prefix(Component.text("      "))
            fakePlayers.forEach {
                addEntry(it.displayName)
            }
        }

        board.registerNewTeam("zNpcs").setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
    }

    private fun addTag(player: Player) {
        Bukkit.getOnlinePlayers().forEach { target ->
            target.scoreboard.getTeam(if (player.isOp) "op" else "regular")!!.addEntry(player.name)
        }
    }

}