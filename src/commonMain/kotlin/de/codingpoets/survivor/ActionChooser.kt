package de.codingpoets.survivor

interface ActionChooser {
    fun chooseAction(sensorState: SensorState) : Action
}

data class SensorState(
    val rounds: Int,
    val timeSinceLastTurn: Int,
    val enemyType: Int,
    val enemyDistance: Int
)

