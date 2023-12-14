package gg.flyte.event.visual.hud.actionbar

import gg.flyte.event.util.miniMessage
import gg.flyte.event.visual.hud.Font
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull

/**
 * The ActionBarHUD class represents a heads-up display (HUD) for the action bar.
 *
 * @param player The player associated with the HUD.
 *
 * @property elements The list of action bar elements to be displayed on the action bar.
 * @property actionBar The formatted Component representation of the action bar.
 * @property changed Indicates whether the action bar has changed and needs to be rebuilt.
 */
class ActionBarHUD(private val player: Player) {

    private var elements = arrayListOf<ActionBarElement>()
    private var actionBar = Component.text("")
    private var changed = true
    var enabled = true

    /**
     * Adds a non-null action bar element to the list of elements.
     *
     * @param element The action bar element to add.
     */
    fun add(@NotNull element: ActionBarElement) {
        remove(element.id)
        elements.add(element)
        changed = true
    }

    /**
     * Removes an element with the specified ID from the collection.
     *
     * @param id The ID of the element to remove.
     * @return true if the element was successfully removed, false otherwise.
     */
    fun remove(@NotNull id: String): Boolean {
        val success = elements.removeIf { it.id == id }
        changed = success || changed
        return success
    }

    /**
     * Updates an action bar element in the elements list.
     *
     * @param element The action bar element to update.
     * @return Returns true if the element was found and updated, false otherwise.
     */
    fun update(@NotNull element: ActionBarElement): Boolean {
        val index = elements.indexOfFirst { it.id == element.id }

        return if (index != -1) {
            elements[index] = element
            changed = true
            true
        } else false
    }

    /**
     * Builds and returns a Component representing the action bar.
     *
     * @return The action bar Component.
     */
    fun build(): Component {
        if (!enabled) return Component.empty()
        var offset = 0

        actionBar = Component.text("")

        var removeWidth = 0

        elements.sortBy {
            it.offset
        }

        elements.forEach {
            offset += it.leftOffset

            var editedText = it.text
            Font.iconTranslations.forEach { translation ->
                editedText = editedText.replace(translation.toTranslate, translation.icon.toString())
            }

            val truncatedText = it.text.dropLast(2)
            val spaces = offset - Font.getTextWidth(truncatedText) - truncatedText.length
            // println("${it.id}: $spaces")

            if (offset != 0) actionBar =
                actionBar.append("<font:learnspigot:ui_space><lang:space.$spaces:></font>".miniMessage)
            actionBar = actionBar.append("<font:learnspigot:hud_actionbar>$editedText</font>".miniMessage)
            offset = it.rightOffset

            removeWidth += Font.getTextWidth(truncatedText)
        }

        if (offset != 0) actionBar =
            actionBar.append("<font:learnspigot:ui_space><lang:space.$offset:></font>".miniMessage)
        changed = false

        return actionBar
    }
}