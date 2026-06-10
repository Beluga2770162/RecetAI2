plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services) // Indispensable para inyectar el google-services.json de Firebase
    alias(libs.plugins.kotlin.compose)  // Habilita el compilador nativo de Compose
}

android {
    namespace = "com.example.recetai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recetai"
        minSdk = 26 // Android 8.0: Excelente cobertura del mercado global
        targetSdk = 35

        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // ACTIVADO: Reduce el tamaño del APK final hasta un 40% y protege tu propiedad intelectual
            isMinifyEnabled = true
            // Elimina recursos y layouts que no se usen en el código final
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Actualizado a Java 17 para máxima compatibilidad con APIs modernas de Android 15+ y Compose
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // ==========================================
    // Android Core & Lifecycle
    // ==========================================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Iconos extendidos para dar diseño premium a menús y configuraciones
    implementation("androidx.compose.material:material-icons-extended")

    // ==========================================
    // Jetpack Compose (Gestionado mediante BOM)
    // ==========================================
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ==========================================
    // Navegación
    // ==========================================
    implementation(libs.androidx.navigation.compose)

    // ==========================================
    // Arquitectura & UI de Soporte
    // ==========================================
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.core:core-splashscreen:1.0.1") // API nativa de arranque de Android

    // ==========================================
    // Ecosistema Firebase (Centralizado mediante BOM)
    // ==========================================
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // Autenticación Avanzada / Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // ==========================================
    // Motor de Hardware: CameraX
    // ==========================================
    implementation("androidx.camera:camera-core:1.3.4")
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    // ==========================================
    // Inteligencia Artificial Local: Google ML Kit
    // ==========================================
    implementation("com.google.mlkit:image-labeling:17.0.9")
    implementation("com.google.mlkit:object-detection:17.0.2")

    // ==========================================
    // Inteligencia Artificial Generativa (Gemini)
    // ==========================================
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    // ==========================================
    // Corrutinas (Programación asíncrona)
    // ==========================================
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ==========================================
    // Gestión de Imágenes e Internet Cache: Coil
    // ==========================================
    implementation("io.coil-kt:coil-compose:2.7.0")

    // ==========================================
    // Entornos de Pruebas (Testing)
    // ==========================================
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

}