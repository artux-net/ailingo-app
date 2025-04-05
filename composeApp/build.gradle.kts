import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.ComposeHotRun
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.hotReload)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.sqlDelight)
    id("kotlin-parcelize")
    alias(libs.plugins.detekt)
}

detekt {
    autoCorrect = true
}

kotlin {
    jvmToolchain(11)
    androidTarget {
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    jvm()

    /* TODO IOS
    listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                baseName = "ailingo"
                isStatic = true
    }
     */

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kermit)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.serialization.json)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.composee)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.coil.compose)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)
            implementation(libs.kotlinx.datetime)
            implementation(libs.composeIcons.featherIcons)
            implementation(libs.sqldelight.coroutines.extension)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(libs.compose.adaptive)
            implementation(libs.compose.adaptive.layout)
            implementation(libs.compose.adaptive.navigation)
            implementation(libs.compose.m3.windowSizeClass)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.android.driver)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.androidx.m3.adaptiveNavSuite)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.driver)
            //Logger
            implementation(libs.slf4j.simple)
            //Playing audio
            implementation(libs.jlayer)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
            // sqlDelight for local database
            implementation(libs.sqldelight.webworker.driver)
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.2"))
            implementation(npm("sql.js", "1.8.0"))
            implementation(npm("copy-webpack-plugin", "11.0.0"))
            implementation(npm("@sqlite.org/sqlite-wasm", "3.43.2-build1"))

        }

        /* TODO IOS
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqlDelight.driver.nativeDriver)
            }
        }*/

        targets.all {
            compilations.all {
                compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.add("-Xexpect-actual-classes")
                    }
                }
            }
        }
    }
}

android {
    namespace = "org.ailingo.app"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        applicationId = "org.ailingo.composeApp.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

compose.desktop {
    application {
        mainClass = "org.ailingo.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AiLingo"
            packageVersion = "1.0.0"

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/MacosIcon.icns"))
                bundleID = "org.ailingo.app.desktopApp"
            }

            modules("java.sql")

            windows {
                menuGroup = "Compose Examples"
                // https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "BF9CDA6A-1391-46D5-9ED5-383D6E68CCEB"
            }
        }
    }
}

//https://github.com/JetBrains/compose-hot-reload
composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}
tasks.register<ComposeHotRun>("runHot") {
    mainClass.set("org.ailingo.app.MainKt")
}

tasks {
    withType<org.gradle.jvm.tasks.Jar> {
        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    }
}

allprojects {
    detekt {
        buildUponDefaultConfig = true
        allRules = true
    }
    tasks.withType<Detekt> {
        setSource(files(project.projectDir))
        exclude("**/build/**")
        exclude {
            it.file.relativeTo(projectDir).startsWith(project.buildDir.relativeTo(projectDir))
        }
    }
    tasks.register("detektAll") {
        dependsOn(tasks.withType<Detekt>())
    }
}

buildConfig {
//    FOR PROD
    buildConfigField("BASE_URL", "https://app.artux.net/ailingo")

//    FOR TESTING
//    buildConfigField("BASE_URL", "http://localhost:8080/ailingo")

    buildConfigField("API_ENDPOINT_USER", "/api/v1/user")
    buildConfigField("API_ENDPOINT_TOPICS", "/api/v1/topics")
    buildConfigField(
        "API_KEY_DICTIONARY",
        "dict.1.1.20231102T140345Z.9979700cf66f91d0.b210308b827953080f07e8f2e12779e2486d2695"
    )
    buildConfigField("BASE_URL_YANEX_DICTIONARY", "https://dictionary.yandex.net/api/v1/dicservice.json/lookup")
    buildConfigField("BASE_URL_FREE_DICTIONARY", "https://api.dictionaryapi.dev/api/v2/entries/en")
    buildConfigField("PREDICTOR_BASE_URL", "https://api.typewise.ai/latest/completion/complete")
    buildConfigField("BASE_URL_UPLOAD_IMAGE", "https://api.imgbb.com/1/upload")
    buildConfigField("UPLOAD_IMAGE_KEY", "f90248ad8f4b1e262a5e8e7603645cc1")
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("org.ailingo.app")
            generateAsync.set(true)
        }
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
    //https://developer.android.com/develop/ui/compose/testing#setup
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
}