package com.learnspigot.event.engine.game

import com.learnspigot.event.engine.Game
import com.learnspigot.event.util.NBSSongType
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class MusicalMinecartsGame : Game() {

    private lateinit var music: RadioSongPlayer

    override fun start() {
        val song = NBSSongType.entries.random()
        music = RadioSongPlayer(song.song)
        music.repeatMode = RepeatMode.ALL

        Bukkit.getOnlinePlayers().forEach(music::addPlayer)

        Bukkit.broadcast(Component.text("now playing ${song.title}"))


        newRound()
    }

    private fun newRound() {
        music.isPlaying = true


    }

    override fun stop() {

    }

    override fun onPlayerJoin(player: Player) {

    }

    override fun onPlayerQuit(player: Player) {

    }

}