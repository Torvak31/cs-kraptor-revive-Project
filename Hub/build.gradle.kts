plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        targetSdk = 35

        // Plugin metadata - change these values for your plugin
        aaptValue("description", "A unified hub for Turkish streaming sites with TMDB catalog integration")
        aaptValue("authors", "Torvak31(Eknux)")
        aaptValue("status", "1") // 0=Down, 1=Ok, 2=Slow, 3=Beta-only
        aaptValue("tvTypes", "Movie/TV Series/Animation")
        aaptValue("requiresResources", "false")
        aaptValue("language", "tr")
        aaptValue("iconUrl", "")
        aaptValue("version", "1")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // CloudStream library - fetched automatically
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
        }
    }
}

tasks.register("make") {
    dependsOn("assembleRelease")
}

tasks.register("makePluginsJson") {
    dependsOn("make")
    doLast {
        val name = rootProject.name
        val description = android.defaultConfig.aaptValues["description"] ?: "CloudStream Plugin"
        val authors = android.defaultConfig.aaptValues["authors"] ?: "Unknown"
        val status = android.defaultConfig.aaptValues["status"] ?: "1"
        val tvTypes = android.defaultConfig.aaptValues["tvTypes"] ?: "Movie"
        val language = android.defaultConfig.aaptValues["language"] ?: "en"
        val iconUrl = android.defaultConfig.aaptValues["iconUrl"] ?: ""
        
        val jsonContent = """
            [
                {
                    "name": "${"$"}{name}",
                    "description": "${"$"}{description}",
                    "authors": "${"$"}{authors}",
                    "status": ${"$"}{status},
                    "tvTypes": "${"$"}{tvTypes}",
                    "language": "${"$"}{language}",
                    "iconUrl": "${"$"}{iconUrl}",
                    "version": 1,
                    "apiVersion": 1,
                    "repositoryUrl": "https://github.com/Torvak31/cs-kraptor-revive-Project",
                    "url": "https://raw.githubusercontent.com/Torvak31/cs-kraptor-revive-Project/builds/${"$"}{name}.cs3",
                    "fileSize": 0
                }
            ]
        """.trimIndent()
        
        File(project.buildDir, "plugins.json").writeText(jsonContent)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xskip-metadata-version-check"
    }
}