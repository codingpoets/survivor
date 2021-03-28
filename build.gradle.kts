import com.soywiz.korge.gradle.*

buildscript {
	val korgePluginVersion: String by project

	repositories {
		mavenLocal()
		maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
		maven { url = uri("https://plugins.gradle.org/m2/") }
		mavenCentral()

		google()
	}
	dependencies {
		classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
	}
}
apply<KorgeGradlePlugin>()

plugins {
	kotlin("multiplatform") version "1.4.31"
}

repositories {
	maven { url = uri("https://kotlin.bintray.com/kotlin-datascience")	}
}

kotlin {
	sourceSets {
		val jvmMain by getting {
			dependencies {
				implementation("org.jetbrains.kotlin-deeplearning:api:0.1.1")
				implementation("org.jetbrains.kotlinx:multik-api:0.0.1")
				implementation("org.jetbrains.kotlinx:multik-default:0.0.1")
			}
		}
	}
}


korge {
	id = "de.codingpoets.survivor"
// To enable all targets at once

	//targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets

	targetJvm()
	targetJs()
	targetDesktop()
	targetIos()
	targetAndroidIndirect() // targetAndroidDirect()
}