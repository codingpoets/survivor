package de.codingpoets.survivor.ui

import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launch
import de.codingpoets.survivor.*

class SurvivorScene() : Scene() {
    override suspend fun Container.sceneInit() {
        // load actionChooser
        val actionChooser = NeuralNetwork()


        fun Direction.display(sim: Simulation) {
            sim.enemies[this]!!.forEach {
                val color = when (it.type) {
                    Enemy.Type.A -> Colors.RED
                    Enemy.Type.B -> Colors.BLUE
                    Enemy.Type.C -> Colors.GREEN
                }
                solidRect(PLAYER_WIDTH, PLAYER_HEIGHT, color).position(it.posX(this), SCENE_HEIGHT - PLAYER_HEIGHT)
            }
            sim.bullets[this]!!.forEach {
                solidRect(PLAYER_WIDTH, PLAYER_HEIGHT / 2, Colors.YELLOW)
                    .position(it.posX(this), SCENE_HEIGHT - PLAYER_HEIGHT / 2)
            }
        }
        fun displaySim(sim: Simulation, complete: Boolean = true) {
            removeChildren()
            // draw player
            solidRect(PLAYER_WIDTH, PLAYER_HEIGHT, if (sim.direction == Direction.LEFT) Colors.PURPLE else Colors.CYAN)
                .position(SCENE_WIDTH / 2 - PLAYER_WIDTH / 2, SCENE_HEIGHT - PLAYER_HEIGHT)

            if (complete) {
                Direction.LEFT.display(sim)
                Direction.RIGHT.display(sim)
            } else {
                sim.direction.display(sim)
            }
            // draw game info/stats
            text("Remaining rounds in rifle: ${sim.rounds}", textSize = 16.0, color = Colors.WHITE).position(20, 20)
            text("Score: ${sim.score}").position(20, 40)
        }

        val sim = Simulation()
        var gameStatus = GameStatus.RUNNING

        val actionInput = setOf(
            Action.SHOOT,
            Action.SHOOT,
            Action.SHOOT,
            Action.TURN,
            Action.RELOAD,
        )

        // GAME
        addFixedUpdater(20.timesPerSecond) {
            if (gameStatus != GameStatus.GAMEOVER) {
//                var action = de.codingpoets.survivor.Action.NOOP
//
//                keys.typed(Key.SPACE) {
//                    print("SPACE Key pressed...")
//                    action = de.codingpoets.survivor.Action.SHOOT
//                }
//                if (views.input.keys[Key.SPACE]) {
//                    print("SPACE Key pressed...")
//                    action = de.codingpoets.survivor.Action.SHOOT
//                }

//                val action = actionChooser.chooseAction(sim.getSensorState())
                sim.run(actionInput.random())
//                sim.run(action)
                gameStatus = sim.gameStatus

                displaySim(sim, false)

            } else {
                text("GAME OVER | Score: ${sim.score}", textSize = 40.0).also { it.position(SCENE_WIDTH / 2 - it.width / 2, SCENE_HEIGHT / 2 - it.height / 2) }
                print("Restarting game...")
                this@SurvivorScene.launch {
                    sceneContainer.changeTo<SurvivorScene>()
                }
            }
        }
    }
}