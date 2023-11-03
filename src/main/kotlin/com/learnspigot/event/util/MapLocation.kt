package com.learnspigot.event.util

import com.learnspigot.event.ChristmasEvent.Companion.WORLD
import org.bukkit.Location

data class MapLocation(
    val x: Number,
    val y: Number,
    val z: Number,
    val yaw: Number = 0,
    val pitch: Number = 0
) : Location(WORLD, x.toDouble(), y.toDouble(), z.toDouble(), yaw.toFloat(), pitch.toFloat())