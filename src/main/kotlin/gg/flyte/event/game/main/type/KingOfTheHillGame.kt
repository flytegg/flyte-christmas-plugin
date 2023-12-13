package gg.flyte.event.game.main.type

import gg.flyte.event.game.GameType
import gg.flyte.event.game.main.MainGame
import gg.flyte.event.game.main.MainGameEngine
import gg.flyte.event.game.main.MainGameEngine.points
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.*
import gg.flyte.twilight.scheduler.repeat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat
import java.time.Duration

class KingOfTheHillGame : MainGame(GameType.KING_OF_THE_HILL) {

    private val RESPAWN_Y = 78
    private var GAME_SECONDS = 120

    init {
        alive.forEach {
            points[it.uniqueId] = 0
        }
    }

    override fun events() {
        events += event<PlayerDropItemEvent> {
            isCancelled = true
        }

        events += event<InventoryClickEvent> {
            isCancelled = true
        }

        events += event<EntityDamageEvent>(priority = EventPriority.HIGHEST) {
            isCancelled = cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
            damage = 0.0
        }

        events += event<EntityRegainHealthEvent> {
            isCancelled = true
            amount = 0.0
        }
    }

    private val df = DecimalFormat("0.0")

    private fun gameLoop() {
        val onHill = mutableListOf<Player>()
        alive.applyForEach {
            if (GameType.KING_OF_THE_HILL.region.contains(location)) {
                onHill.add(this)
            }
        }

        if (onHill.size == 1) {
            Bukkit.getOnlinePlayers().applyForEach {
                resetTitle()
            }

            onHill[0].apply {
                val newPoints = points[uniqueId]!! + 50
                sendActionBar(text().append(
                    text("${df.format(newPoints / 1000.0)}s held").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
                    text(" | ").color(NamedTextColor.GRAY),
                    text("$GAME_SECONDS second" + (if (GAME_SECONDS == 1) "" else "s") + " left").color(NamedTextColor.DARK_AQUA).decorate(TextDecoration.BOLD)
                ).build())
                points[uniqueId] = newPoints
            }
        } else if (onHill.size > 1) {
            Bukkit.getOnlinePlayers().applyForEach {
                showTitle(Title.title(
                    text("CONTESTED!").color(NamedTextColor.RED),
                    Component.empty(),
                    Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ZERO)
                ))
            }
        }
    }

    override fun start() {
        val knockbackStick = ItemStack(Material.STICK).apply {
            addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
            val meta = itemMeta
            meta.displayName(text("Knockback Stick").color(NamedTextColor.WHITE))
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            itemMeta = meta
        }

        alive.applyForEach {
            getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 10.0;
            health = 10.0
            inventory.addItem(knockbackStick)
        }

        tasks += repeat(1, 1) {
            gameLoop()
        }

        tasks += repeat(4, 4) {
            val iterator = alive.iterator()
            while (iterator.hasNext()) {
                val player = iterator.next()
                if (player.location.y <= RESPAWN_Y) {
                    if (player.health == 2.0) {
                        player.apply {
                            inventory.storageContents = emptyArray()
                            world.strikeLightning(player.location)
                            teleport(GameType.KING_OF_THE_HILL.spectatorSpawn!!)
                            playSound(Sound.ENTITY_PLAYER_DEATH)
                            getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0;
                            health = 20.0
                            resetTitle()
                            clearActionBar()
                        }
                        iterator.remove()
                        Bukkit.broadcastMessage("${player.name} was elmimnated")

                        if (alive.size == 1) {
                            Bukkit.broadcastMessage("game ended, ${alive[0]!!.name} last man standing")
                            MainGameEngine.stop()
                            return@repeat
                        }
                    } else {
                        player.apply {
                            health = player.health - 2
                            teleport(GameType.KING_OF_THE_HILL.spawns[0].random())
                            sendMessage("you fell, lost another life")
                        }
                    }
                }
            }
        }

        tasks += repeat(0, 20) {
            if (GAME_SECONDS == 0) {
                Bukkit.broadcastMessage("game has ended, someone won, gg")
                MainGameEngine.stop()
                return@repeat
            }

            Bukkit.getOnlinePlayers().applyForEach {
                sendActionBar(text().append(
                    text("${df.format(points[uniqueId]!! / 1000.0)}s held").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
                    text(" | ").color(NamedTextColor.GRAY),
                    text("$GAME_SECONDS second" + (if (GAME_SECONDS == 1) "" else "s") + " left").color(NamedTextColor.DARK_AQUA).decorate(TextDecoration.BOLD)
                ).build())
            }

            GAME_SECONDS--
        }
    }

    override fun stop() {
        Bukkit.getOnlinePlayers().applyForEach {
            getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0;
            health = 20.0
            inventory.storageContents = emptyArray()
            resetTitle()
            clearActionBar()
        }
    }

    override fun onPlayerJoin(player: Player) {
        TODO("Not yet implemented")
    }

    override fun onPlayerQuit(player: Player) {
        TODO("Not yet implemented")
    }

}