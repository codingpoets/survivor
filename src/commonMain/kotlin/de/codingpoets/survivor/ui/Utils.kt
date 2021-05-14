package de.codingpoets.survivor.ui

import de.codingpoets.survivor.*

const val SCENE_WIDTH = 1000
const val SCENE_HEIGHT = 100
const val PLAYER_WIDTH = 10
const val PLAYER_HEIGHT = 10

fun Drawable.posX(direction: Direction): Int {
    return when (direction) {
        Direction.LEFT -> (SCENE_WIDTH / 2 - normalizedPosition * SCENE_WIDTH / 2).toInt()
        Direction.RIGHT -> (SCENE_WIDTH / 2 + normalizedPosition * SCENE_WIDTH / 2).toInt()
    }
}

