package gg.flyte.event.visual.hud

import gg.flyte.event.util.miniMessage
import gg.flyte.event.util.plainText

object Font {

    /**
     * List of all icon translations. An icon translation is used to replace e.g. 'C' to a unicode icon
     */
    val iconTranslations = arrayListOf<IconTranslation>()

    /**
     * Get width of a standard character
     *
     * @param character Character to get the width of
     * @return Width of the given character
     */
    fun getWidth(character: Char): Int = FontInfo.entries.first { it.character == character }.width

    /**
     * Calculates the width of a given text.
     *
     * @param text The text to calculate the width of.
     * @return The width of the given text.
     */
    fun getTextWidth(text: String): Int {
        var width = 0

        text.miniMessage.plainText.toCharArray().forEach { character ->
            width += if (!iconTranslations.any { iconTranslation -> iconTranslation.toTranslate == character.toString() }) {
                FontInfo.entries.firstOrNull { it.character == character  }?.width ?: 0
            } else {
                iconTranslations.first { iconTranslation -> iconTranslation.toTranslate == character.toString() }.width
            }
        }

        return width
    }

    /**
     * Class containing all widths bound to a character for a specific font
     *
     * @property character Character to bind width to
     * @property width Width to bind to character
     */
    enum class FontInfo(
        val character: Char,
        val width: Int
    ) {
        A('A', 5), a('a', 5), B('B', 5), b('b', 5),
        C('C', 5), c('c', 5), D('D', 5), d('d', 5),
        E('E', 5), e('e', 5), F('F', 5), f('f', 4),
        G('G', 5), g('g', 5), H('H', 5), h('h', 5),
        I('I', 3), i('i', 1), J('J', 5), j('j', 5),
        K('K', 5), k('k', 4), L('L', 5), l('l', 2),
        M('M', 5), m('m', 5), N('N', 5), n('n', 5),
        O('O', 5), o('o', 5), P('P', 5), p('p', 5),
        Q('Q', 5), q('q', 5), R('R', 5), r('r', 5),
        S('S', 5), s('s', 5), T('T', 5), t('t', 3),
        U('U', 5), u('u', 5), V('V', 5), v('v', 5),
        W('W', 5), w('w', 5), X('X', 5), x('x', 5),
        Y('Y', 5), y('y', 5), Z('Z', 5), z('z', 5),
        NUM_1('1', 5), NUM_2('2', 5), NUM_3('3', 5),
        NUM_4('4', 5), NUM_5('5', 5), NUM_6('6', 5),
        NUM_7('7', 5), NUM_8('8', 5), NUM_9('9', 5),
        NUM_0('0', 5), EXCLAMATION_POINT('!', 1), AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5), DOLLAR_SIGN('$', 5), PERCENT('%', 5),
        UP_ARROW('^', 5), AMPERSAND('&', 5), ASTERISK('*', 3),
        LEFT_PARENTHESIS('(', 3), RIGHT_PARENTHESIS(')', 3), MINUS('-', 5),
        UNDERSCORE('_', 5), PLUS_SIGN('+', 5), EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 3), RIGHT_CURL_BRACE('}', 3), LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3), COLON(':', 1), SEMI_COLON(';', 1),
        DOUBLE_QUOTE('\"', 3), SINGLE_QUOTE('\'', 1), LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4), QUESTION_MARK('?', 5), SLASH('/', 5),
        BACK_SLASH('\\', 5), LINE('|', 1), TILDE('~', 5),
        TICK('`', 2), PERIOD('.', 1), COMMA(',', 1),
        SPACE(' ', 3), IN_BETWEEN(' ', 3), DEFAULT('默', 8)
    }
}