package gg.flyte.event.visual.hud.impl

import gg.flyte.event.game.main.MainGameEngine
import gg.flyte.event.visual.hud.Alignment
import gg.flyte.event.visual.hud.Font
import gg.flyte.event.visual.hud.IconTranslation
import gg.flyte.event.visual.hud.actionbar.ActionBarElement
import gg.flyte.event.visual.hud.actionbar.ActionBarHUD
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

object ActionBarImpl {

    private val actionBarHUDs = hashMapOf<UUID, ActionBarHUD>()
    private val runningSchedulers = hashMapOf<UUID, BukkitTask>()

    fun register(player: Player) {
        val uuid = player.uniqueId
        val actionBarHUD = ActionBarHUD(player)
        actionBarHUD.enabled = false

        actionBarHUDs[uuid] = actionBarHUD

        Font.iconTranslations.add(IconTranslation("P", '', 8))
        Font.iconTranslations.add(IconTranslation("C", '', 8))

        val xpComponent = ActionBarElement("xp", 108, "<#4aab33>0</#4aab33>P", Alignment.RIGHT)

        actionBarHUD.add(xpComponent)

        val scheduler = gg.flyte.twilight.scheduler.repeat {
            if (MainGameEngine.points.containsKey(uuid)) {
                if (!actionBarHUD.enabled) actionBarHUD.enabled = true
                xpComponent.text = "${MainGameEngine.points[uuid]}P"
            } else {
                if (actionBarHUD.enabled) actionBarHUD.enabled = false
            }

            actionBarHUD.update(xpComponent)

            player.sendActionBar(actionBarHUD.build())
        }

        runningSchedulers[uuid] = scheduler
    }

    fun unregister(player: Player) {
        actionBarHUDs.remove(player.uniqueId)
        runningSchedulers[player.uniqueId]?.cancel()
        runningSchedulers.remove(player.uniqueId)
    }
}