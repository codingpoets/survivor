package de.codingpoets.survivor

actual class NeuralNetwork actual constructor() : ActionChooser {

    override fun chooseAction(sensorState: SensorState) : Action {
//        TODO("Not yet implemented")
        return Action.SHOOT
    }
}