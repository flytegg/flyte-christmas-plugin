package gg.flyte.event.visual.hud

/**
 * An icon translation is used to replace e.g. 'C' to a unicode icon
 *
 * @param toTranslate Text or character (doesn't matter, both are possible) to translate. In the above example, 'C'
 * @param icon Icon to translate the 'toTranslate' property to. In the above example 'C' would be translated to the
 *             given icon
 * @param width Width of the given 'icon' parameter, since in most of the cases a unicode character is used and these
 *              can't be registered as an enum entry
 */
data class IconTranslation(val toTranslate: String, val icon: Char, val width: Int)