package gg.flyte.event.util

import com.xxmicloxx.NoteBlockAPI.model.Song
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import java.io.File

private fun parse(fileName: String): Song {
    return NBSDecoder.parse(File("${gg.flyte.event.ChristmasEvent.INSTANCE.dataFolder}/music", fileName))
}

enum class NBSSongType(val title: String, val song: Song) {

    JINGLE_BELLS("Jingle Bells", parse("jingle_bells.nbs")),
    JOY_TO_THE_WORLD("Joy To The World", parse("joy_to_the_world.nbs")),
    O_COME_ALL_YE_FAITHFUL("O' Come All Ye Faithful", parse("o_come_all_ye_faithful.nbs")),
    SILENT_NIGHT("Silent Night", parse("silent_night.nbs")),
    WE_WISH_YOU_A_MERRY_CHRISTMAS("We Wish You A Merry Christmas", parse("we_wish_you_a_merry_christmas.nbs"));

}