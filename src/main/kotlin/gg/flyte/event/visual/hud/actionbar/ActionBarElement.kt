package gg.flyte.event.visual.hud.actionbar

import gg.flyte.event.visual.hud.Font
import gg.flyte.event.visual.hud.Alignment

/**
 * Represents an element in the action bar.
 *
 * @param id The unique identifier of the action bar element.
 * @param offset The offset of the element from the edge of the action bar.
 * @param text The text displayed in the element.
 * @param alignment The alignment of the element within the action bar.
 *
 * @property width The width of the element's text.
 * @property leftOffset The offset of the element from the left edge of the action bar.
 * @property rightOffset The offset of the element from the right edge of the action bar.
 */
class ActionBarElement(val id: String, val offset: Int, var text: String, alignment: Alignment) {

    val width = Font.getTextWidth(text)
    var leftOffset: Int = 0
    var rightOffset: Int = 0

    init {

        when (alignment) {
            Alignment.LEFT -> {
                leftOffset = offset
                rightOffset = -offset - width
            }
            Alignment.RIGHT -> {
                leftOffset = offset - width
                rightOffset = -offset
            }
            Alignment.CENTER -> {
                leftOffset = offset - width / 2
                rightOffset = -leftOffset - width
            }
        }
    }
}