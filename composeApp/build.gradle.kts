import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.2.2")
    }
}

with(tasks) {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
}

plugins {
    id("com.louiscad.complete-kotlin") version "1.1.0"
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            //Ktor core
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-java:2.3.7")

            //ktor logging
            implementation("ch.qos.logback:logback-classic:1.4.12")
            implementation("io.ktor:ktor-client-logging:2.3.7")

            //ktor content negotiator
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")

            //Serialization
            implementation("io.ktor:ktor-serialization-gson:2.3.7")

            //Gson
            implementation("com.google.code.gson:gson:2.10.1")

            //QR
            implementation("io.github.alexzhirkevich:qrose:1.0.0-beta3")

            //Kamel
            implementation("media.kamel:kamel-image:0.9.1")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.auxonode"
            packageVersion = "1.0.0"
            modules("java.instrument", "java.management", "java.net.http", "java.sql", "jdk.unsupported")
        }
        buildTypes.release.proguard {
            isEnabled.set(true)
            obfuscate.set(true)
        }
    }
}