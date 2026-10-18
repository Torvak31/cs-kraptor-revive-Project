plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        
        // CloudStream plugin metadata
        manifestPlaceholders["description"] = "Turkish streaming sites hub with TMDB integration"
        manifestPlaceholders["authors"] = "Torvak31(Eknux)"
        manifestPlaceholders["status"] = "1"
        manifestPlaceholders["tvTypes"] = "Movie|TV Series|Animation"
        manifestPlaceholders["requiresResources"] = "false"
        manifestPlaceholders["language"] = "tr"
        manifestPlaceholders["iconUrl"] = ""
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
}

// Custom task to generate plugins.json after assembleRelease
tasks.register("makePluginsJson") {
    dependsOn("assembleRelease")
    doLast {
        val jsonContent = """
            [
                {
                    "name": "Hub",
                    "description": "Turkish streaming sites hub with TMDB integration",
                    "authors": "Torvak31(Eknux)",
                    "status": 1,
                    "tvTypes": "Movie|TV Series|Animation",
                    "language": "tr",
                    "iconUrl": "",
                    "version": 1,
                    "apiVersion": 1,
                    "repositoryUrl": "https://github.com/Torvak31/cs-kraptor-revive-Project",
                    "url": "https://raw.githubusercontent.com/Torvak31/cs-kraptor-revive-Project/builds/Hub.cs3",
                    "fileSize": 0
                }
            ]
        """.trimIndent()
        
        File(project.layout.buildDirectory.get().asFile, "plugins.json").writeText(jsonContent)
    }
}

// Override make task to depend on assembleRelease
tasks.named("make") {
    dependsOn("assembleRelease")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xskip-metadata-version-check"
    }
}