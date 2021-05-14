package de.codingpoets.survivor.ui

import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korim.color.Colors
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import kotlin.reflect.KClass

object ConfigModule : Module() {
    override val bgcolor = Colors.BLACK
    override val windowSize = SizeInt(1024, 312)
    override val size = SizeInt(SCENE_WIDTH, SCENE_HEIGHT)
    override val clipBorders = true
    override val mainScene : KClass<out Scene> = SurvivorScene::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype { SurvivorScene() }
    }
}