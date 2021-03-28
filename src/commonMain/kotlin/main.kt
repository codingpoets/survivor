import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.*
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import kotlin.reflect.KClass

const val SCENE_WIDTH = 1000
const val SCENE_HEIGHT = 100
const val PLAYER_WIDTH = 10
const val PLAYER_HEIGHT = 10

suspend fun main() = Korge(Korge.Config(module = ConfigModule))

object ConfigModule : Module() {
    override val bgcolor = Colors.BLACK
    override val windowSize = SizeInt(1024, 312)
    override val size = SizeInt(SCENE_WIDTH, SCENE_HEIGHT)
    override val clipBorders = true
    override val mainScene : KClass<out Scene> = SurvivorScene::class

    override suspend fun  AsyncInjector.configure() {
        mapPrototype { SurvivorScene() }
    }
}

class SurvivorScene() : Scene() {
    override suspend fun Container.sceneInit() {
        fun displaySimulation(sim: Simulation) {
            // clear all objects from container
            removeChildren()
            // draw player
            solidRect(PLAYER_WIDTH, PLAYER_HEIGHT, if (sim.direction == Direction.LEFT) Colors.PURPLE else Colors.CYAN )
                .position(SCENE_WIDTH / 2 - PLAYER_WIDTH / 2, SCENE_HEIGHT - PLAYER_HEIGHT)
            // draw enemies
            sim.enemies[Direction.LEFT]!!.forEach {
                val color = when (it.type) {
                    Enemy.Type.A -> Colors.RED
                    Enemy.Type.B -> Colors.BLUE
                    Enemy.Type.C -> Colors.GREEN
                }
                solidRect(PLAYER_WIDTH, PLAYER_HEIGHT, color)
                    .position((SCENE_WIDTH / 2 - it.normalizedPosition * SCENE_WIDTH / 2).toInt(), SCENE_HEIGHT - PLAYER_HEIGHT)
            }
            sim.enemies[Direction.RIGHT]!!.forEach {
                val color = when (it.type) {
                    Enemy.Type.A -> Colors.RED
                    Enemy.Type.B -> Colors.BLUE
                    Enemy.Type.C -> Colors.GREEN
                }
                solidRect(PLAYER_WIDTH, PLAYER_HEIGHT, color)
                    .position((SCENE_WIDTH / 2 + it.normalizedPosition * SCENE_WIDTH / 2).toInt(), SCENE_HEIGHT - PLAYER_HEIGHT)
            }

            // draw bullets
            sim.bullets[Direction.LEFT]!!.forEach {
                solidRect(PLAYER_WIDTH, PLAYER_HEIGHT / 2, Colors.YELLOW)
                    .position((SCENE_WIDTH / 2 - it.normalizedPosition * SCENE_WIDTH / 2).toInt(), SCENE_HEIGHT - PLAYER_HEIGHT / 2)
            }
            sim.bullets[Direction.RIGHT]!!.forEach {
                solidRect(PLAYER_WIDTH, PLAYER_HEIGHT / 2, Colors.YELLOW)
                    .position((SCENE_WIDTH / 2 + it.normalizedPosition * SCENE_WIDTH / 2).toInt(), SCENE_HEIGHT - PLAYER_HEIGHT / 2)
            }

            // draw game info/stats
            text("Remaining rounds in rifle: ${sim.rounds}", textSize = 16.0, color = Colors.WHITE).position(20, 20)
            text("Score: ${sim.score}").position(20, 40)

        }

        val sim = Simulation()
        val actionInput = listOf(
                Action.SHOOT,
                Action.SHOOT,
                Action.SHOOT,
                Action.SHOOT,
                Action.SHOOT,
                Action.TURN,
                Action.TURN,
                Action.RELOAD,
        )
        var gameStatus = GameStatus.RUNNING
        addFixedUpdater(30.timesPerSecond) {
            if (gameStatus != GameStatus.GAMEOVER) {
                sim.run(actionInput.random())
                println(sim)
                displaySimulation(sim)
                gameStatus = sim.gameStatus
            } else {
                text("GAME OVER", textSize = 40.0).also { it.position(SCENE_WIDTH / 2 - it.width / 2, SCENE_HEIGHT / 2 - it.height / 2) }
            }
        }
    }
}
