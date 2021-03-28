package ml

import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.activation.Activations
import org.jetbrains.kotlinx.dl.api.core.initializer.Constant
import org.jetbrains.kotlinx.dl.api.core.initializer.GlorotNormal
import org.jetbrains.kotlinx.dl.api.core.layer.Dense
import org.jetbrains.kotlinx.dl.api.core.layer.Input
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import java.io.File

private const val SEED = 12L

private val myNN = Sequential.of(
        Input(4),
        Dense(
                outputSize = 4,
                activation = Activations.Sigmoid,
                kernelInitializer = GlorotNormal(SEED),
                biasInitializer = Constant(0.1f)
        ),
)

fun Sequential.compileLoadAndPredict(model: File = File("Model"), inputData: FloatArray): Int {
    use {
        it.compile(
            optimizer = Adam(),
            loss = Losses.SOFT_MAX_CROSS_ENTROPY_WITH_LOGITS,
            metric = Metrics.ACCURACY,
        )
        it.loadWeights(model)
        return it.predict(inputData)
    }
}

fun Sequential.compileInitAndSaveModel(model: File = File("Model")) {
    use {
        it.compile(
            optimizer = Adam(),
            loss = Losses.SOFT_MAX_CROSS_ENTROPY_WITH_LOGITS,
            metric = Metrics.ACCURACY,
        )
        it.init()
        it.save(model)
    }
}

fun main() {
    val label = myNN.compileLoadAndPredict(File("Model"), floatArrayOf(0.2f, 0.5f, 0.2f, 0.5f))
    println("Predicted label is: $label")
}