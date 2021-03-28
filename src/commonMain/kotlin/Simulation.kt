import kotlin.math.log10

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"

val String.red get() = ANSI_RED + this + ANSI_RESET
val String.blue get() = ANSI_BLUE + this + ANSI_RESET
val String.black get() = ANSI_BLACK + this + ANSI_RESET
val String.green get() = ANSI_GREEN + this + ANSI_RESET
val String.yellow get() = ANSI_YELLOW + this + ANSI_RESET
val String.purple get() = ANSI_PURPLE + this + ANSI_RESET
val String.cyan get() = ANSI_CYAN + this + ANSI_RESET
val String.white get() = ANSI_WHITE + this + ANSI_RESET

const val MAX_ROUNDS = 12
const val START_DISTANCE = 1000
const val INITIAL_ENEMY_TIMER = 20

interface Drawable {
    val position: Int
    val symbol: String
    val normalizedPosition get() = position.toDouble() / START_DISTANCE
}

class Enemy(val type: Type): Drawable  {
    enum class Type {
        A, B, C
    }
    var health = when (type) {
        Type.A -> 1
        Type.B -> 1
        Type.C -> 2
    }
    val movementSpeed = when (type) {
        Type.A -> 10
        Type.B -> 20
        Type.C -> 10
    }
    val scoreValue = when (type) {
        Type.A -> 10
        Type.B -> 20
        Type.C -> 20
    }

    override var position = START_DISTANCE
    override val symbol get() = when (type) {
        Type.A -> "$health".red
        Type.B -> "$health".blue
        Type.C -> "$health".green
    }

    fun update() {
        position -= movementSpeed
    }
}

class SimpleBullet(): Drawable {
    override var position = 0
    override val symbol = "-".yellow
    val movementSpeed = 50

    fun update() {
        position += movementSpeed
    }
}

data class Status(
        val rounds: Int,
        val timeSinceLastTurn: Int,
        val enemyDistance: Int?,
        val enemyType: Enemy.Type?,
        val delay: Int,
        val gameStatus: GameStatus,
        )

enum class Action(val delay: Int) {
//    SHOOT(50), TURN(30), RELOAD(100), NOOP(0)
    SHOOT(2), TURN(1), RELOAD(1), NOOP(0)
}

enum class GameStatus {
    RUNNING, GAMEOVER
}

class Simulation {
    val enemies = mapOf(Direction.LEFT to mutableListOf<Enemy>(), Direction.RIGHT to mutableListOf())
    val bullets = mapOf(Direction.LEFT to mutableListOf<SimpleBullet>(), Direction.RIGHT to mutableListOf())
    var rounds = MAX_ROUNDS
    var direction = Direction.LEFT // player view direction
    var timeSinceLastTurn = 0
    var delay = 0
    var newEnemyTimer = INITIAL_ENEMY_TIMER
    var score = 0
    var playTime = 0
    var gameStatus = GameStatus.RUNNING


    private val Int.scale get() = this / 10

    override fun toString(): String {
        var tmpDirection = Direction.LEFT
        var entities = bullets[tmpDirection]!! + enemies[tmpDirection]!!
        val leftField = (START_DISTANCE.scale downTo 1).map { pos ->
            entities.find { it.position.scale == pos }?.symbol ?: "_"
        }.joinToString("")

        tmpDirection = Direction.RIGHT
        entities = bullets[tmpDirection]!! + enemies[tmpDirection]!!
        val rightField = (1..START_DISTANCE.scale).map { pos ->
            entities.find { it.position.scale == pos }?.symbol ?: "_"
        }.joinToString("")

        val player = when (direction) {
            Direction.LEFT -> "<".purple
            Direction.RIGHT -> ">".cyan
        }

        return leftField + player + rightField
    }

    private fun closestEnemy(direction: Direction): Enemy? {
        return enemies[direction]!!.minByOrNull { it.position }
    }

    private fun startNewEnemyTimer() {
        newEnemyTimer = (20.0 / log10(playTime.toDouble())).toInt()
    }

    private fun updateBulletCollision(direction: Direction) {
        val frontBullet = bullets[direction]!!.firstOrNull()
        val frontEnemy = closestEnemy(direction)
        if (frontBullet != null && frontEnemy != null && frontBullet.position >= frontEnemy.position) {
            bullets[direction]!!.remove(frontBullet)
            if (--frontEnemy.health == 0) {
                score += frontEnemy.scoreValue
                enemies[direction]!!.remove(frontEnemy)
            }
        }
    }

    private fun updatePlayerCollision() {
        Direction.values().forEach {
            val enemy = closestEnemy(it)
            if (enemy != null && enemy.position <= 0) {
                gameStatus = GameStatus.GAMEOVER
                return
            }
        }
    }

    private fun update() {
        bullets.values.flatten().forEach(SimpleBullet::update)
        enemies.values.flatten().forEach(Enemy::update)
        // check and process hitting bullets
        updateBulletCollision(Direction.LEFT)
        updateBulletCollision(Direction.RIGHT)
        updatePlayerCollision()
        listOf(Direction.LEFT, Direction.RIGHT).forEach {
            val bulletsOutOfRange = bullets[it]!!.filter { it.position > START_DISTANCE }
            bullets[it]!!.removeAll(bulletsOutOfRange)
        }
        if (--newEnemyTimer == 0) {
            // create new enemy in random direction
            val newEnemyDirection = Direction.values().random()
            val newEnemyType = Enemy.Type.values().random()
            enemies[newEnemyDirection]!!.add(Enemy(newEnemyType))
            startNewEnemyTimer()
        }

    }

    fun run(action: Action): Status {
        playTime++
        score++
        if (delay == 0) {
            doAction(action)
        } else {
            delay--
        }
        update()
        val enemy = closestEnemy(direction)
        return Status(rounds, timeSinceLastTurn, enemy?.position, enemy?.type, delay, gameStatus)
    }

    private fun doAction(action: Action) {
        when (action) {
            Action.SHOOT -> {
                if (rounds > 0) {
                    bullets[direction]!!.add(SimpleBullet())
                    rounds--
                }
            }
            Action.TURN -> direction = direction.toggle()
            Action.RELOAD -> rounds = MAX_ROUNDS
        }
        delay = action.delay
    }
}

fun main() {
    val sim = Simulation()
    do {
        val status = sim.run(Action.NOOP)
        println(status)
    } while (status.gameStatus != GameStatus.GAMEOVER)
}