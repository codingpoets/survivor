import com.soywiz.korio.async.async
import com.soywiz.korma.random.randomWithWeights
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random

const val MIN_WEIGHT_FACTOR = -4.0
const val MAX_WEIGHT_FACTOR = 4.0
const val MUTATION_RPOBABILITY = 0.05
const val GENERATION_SIZE = 50
const val MATING_POOL_SIZE = 8

suspend fun main() {

    /*
    - Load Models from Last Generation
        - [OPTIONAL] instead create an initial "random" Generation
    - [OPTIONAL] backup last generation folder to *_BCK (in case of corrupted new generation)
    - run simulation
    - evaluate best scoring children
    - populate next generation
        - crossing
        - mutation
    - write new generation to files
    - GOTO
     */

    val models: List<SurvivorModel> = loadModels("GenerationCurrent")
    models.save("GenerationLast")

//    val models = createInitialModels(GENERATION_SIZE)

    val scores = models.map {
        async(Dispatchers.Default) {
            it to runSimulation(it)
        }
    }.map { it.await() }.toMap()

    val nextGen = (0..GENERATION_SIZE).flatMap {
        val first = scores.keys.toList().randomWithWeights(scores.values.map { it.toDouble() })
        val remainingParents = (scores - first)
        val second = remainingParents.keys.toList().randomWithWeights(remainingParents.values.map { it.toDouble() })
        val childFirst = SurvivorModel(
            first.biases crossover second.biases,
            first.weights crossover second.weights
        ).mutate()
        val childSecond = SurvivorModel(
            second.biases crossover first.biases,
            second.weights crossover first.weights
        ).mutate()
        listOf(childFirst, childSecond)
    }

    nextGen.save("GenerationCurrent")

}

fun SurvivorModel.mutate() : SurvivorModel {
    return if (Random.nextDouble() < MUTATION_RPOBABILITY) {
        val mutatedArray = biases + weights
        val mutationIndex = Random.nextInt(mutatedArray.size)
        mutatedArray[mutationIndex] = Random.nextDouble(MIN_WEIGHT_FACTOR, MAX_WEIGHT_FACTOR).toFloat()
        SurvivorModel(mutatedArray.take(biases.size).toFloatArray(), mutatedArray.drop(biases.size).toFloatArray() )
    } else {
        this
    }
}

infix fun FloatArray.crossover(other: FloatArray) : FloatArray {
    val crossoverPoint = size/2
    return (take(crossoverPoint) + other.drop(crossoverPoint)).toFloatArray()
}


expect fun runSimulation(model: SurvivorModel): Int

expect fun loadModels(dir: String): List<SurvivorModel>

expect fun List<SurvivorModel>.save(dir: String)

class SurvivorModel(
    val biases: FloatArray,
    val weights: FloatArray,
)

fun createInitialModels(amount: Int): List<SurvivorModel> {
    return List(amount) {
        val biases = List(4) { Random.nextFloat() }.toFloatArray()
        val weights = List(16) { Random.nextFloat() }.toFloatArray()
        SurvivorModel(biases, weights)
    }
}


//suspend fun main() = Korge(Korge.Config(module = de.codingpoets.survivor.ui.ConfigModule))