package de.codingpoets.survivor.ui

import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container

class ManagementScene() : Scene() {
    override suspend fun Container.sceneInit() {
        // check if another run is needed
            // if so set/link corresponding model files/directory
            // start new scene
            sceneContainer.changeTo<SurvivorScene>()
        // if not proceed with next generation
    }
}