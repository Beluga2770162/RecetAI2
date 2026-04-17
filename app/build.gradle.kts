plugins {

    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}
android {
    namespace = "com.example.recetai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recetai"

        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}


    dependencies {
        // --- 1. CORE ANDROID & COMPOSE ---
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
        implementation("androidx.activity:activity-compose:1.8.2")

        // Bom de Compose (Gestiona versiones de UI, Material3, etc.)
        implementation(platform("androidx.compose:compose-bom:2024.02.01"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")

        // ICONOS: Necesario para los menús de tu diseño
        implementation("androidx.compose.material:material-icons-extended")

        // --- 2. CAMERAX (Escáner de ingredientes) ---
        val cameraxVersion = "1.3.1"
        implementation("androidx.camera:camera-core:$cameraxVersion")
        implementation("androidx.camera:camera-camera2:$cameraxVersion")
        implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
        implementation("androidx.camera:camera-view:$cameraxVersion")

        // --- 3. ML KIT (Inteligencia Artificial local) ---
        implementation("com.google.mlkit:object-detection:17.0.2")
        implementation("com.google.mlkit:image-labeling:17.0.9")

        // --- 4. FIREBASE (Base de datos en la nube) ---
        implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
        implementation("com.google.firebase:firebase-firestore-ktx")

        // --- 5. SOLUCIONES A ERRORES ESPECÍFICOS (The "César Fixes") ---
        // Corrige error de LocalLifecycleOwner
        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")

        // Corrige error de ListenableFuture y addListener
        implementation("com.google.guava:guava:33.0.0-android")

        // Puente para que las corrutinas de Kotlin entiendan a Guava
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.3")
        implementation("androidx.navigation:navigation-compose:2.7.7")
        implementation("androidx.navigation:navigation-compose:2.7.7")
        // --- 6. TESTING ---
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
    }