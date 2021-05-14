import com.soywiz.klock.timesPerSecond
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.input.keys
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

fun Drawable.posX(direction: Direction): Int {
    return when (direction) {
        Direction.LEFT -> (SCENE_WIDTH / 2 - normalizedPosition * SCENE_WIDTH / 2).toInt()
        Direction.RIGHT -> (SCENE_WIDTH / 2 + normalizedPosition * SCENE_WIDTH / 2).toInt()
    }
}

class SurvivorScene() : Scene() {
    override suspend fun Container.sceneInit() {
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
            solidRect(PLAYER_WIDTH, PLAYER_HEIGHT, if (sim.direction == Direction.LEFT) Colors.PURPLE else Colors.CYAN )
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
                var action = Action.NOOP

                keys.typed(Key.SPACE) {
                    print("SPACE Key pressed...")
                    action = Action.SHOOT
                }
                if (views.input.keys[Key.SPACE]) {
                    print("SPACE Key pressed...")
                    action = Action.SHOOT
                }

                sim.run(actionInput.random())
                sim.run(action)
                gameStatus = sim.gameStatus

                displaySim(sim, false)

            } else {
                text("GAME OVER | Score: ${sim.score}", textSize = 40.0).also { it.position(SCENE_WIDTH / 2 - it.width / 2, SCENE_HEIGHT / 2 - it.height / 2) }
            }
        }
    }
}
