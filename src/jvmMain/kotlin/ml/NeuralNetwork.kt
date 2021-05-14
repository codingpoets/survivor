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

fun getNN(): Sequential {
    return Sequential.of(
        Input(4),
        Dense(
            outputSize = 4,
            activation = Activations.Sigmoid,
            kernelInitializer = GlorotNormal(SEED),
            biasInitializer = Constant(0.1f)
        ),
    )
}


fun Sequential.compileAndLoad(model: File = File("Model")) = apply {
    compile(
        optimizer = Adam(),
        loss = Losses.SOFT_MAX_CROSS_ENTROPY_WITH_LOGITS,
        metric = Metrics.ACCURACY,
    )
    loadWeights(model)
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
    val aiModel = getNN().compileAndLoad(File("Model"))
    println("Predicted label is: ${aiModel.predict(floatArrayOf(0.2f, 0.5f, 0.2f, 0.5f))}")
}