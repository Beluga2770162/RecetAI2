plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.compose)
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

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }

    kotlinOptions {

        jvmTarget = "11"
    }

    buildFeatures {

        compose = true
    }
}

dependencies {

// ==========================
// Android Core
// ==========================

    implementation(libs.androidx.core.ktx)

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(
        libs.androidx.activity.compose
    )

    implementation("androidx.compose.material:material-icons-extended")

// ==========================
// Compose
// ==========================

    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    implementation(libs.androidx.ui)

    implementation(
        libs.androidx.ui.graphics
    )

    implementation(
        libs.androidx.ui.tooling.preview
    )

    implementation(
        libs.androidx.material3
    )

// ==========================
// Navigation
// ==========================

    implementation(
        libs.androidx.navigation.compose
    )

// ==========================
// ViewModel
// ==========================

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7"
    )

// ==========================
// Splash Screen
// ==========================

    implementation(
        "androidx.core:core-splashscreen:1.0.1"
    )

// ==========================
// Firebase
// ==========================

    implementation(
        platform(
            libs.firebase.bom
        )
    )

    implementation(
        libs.firebase.analytics
    )

    implementation(
        libs.firebase.auth
    )

    implementation(
        libs.firebase.firestore
    )

// Google Sign In

    implementation(
        "com.google.android.gms:play-services-auth:21.2.0"
    )

// ==========================
// CameraX
// ==========================

    implementation(
        "androidx.camera:camera-core:1.3.4"
    )

    implementation(
        libs.camera.camera2
    )

    implementation(
        libs.camera.lifecycle
    )

    implementation(
        libs.camera.view
    )

// ==========================
// ML Kit
// ==========================

    implementation(
        "com.google.mlkit:image-labeling:17.0.9"
    )

    implementation(
        "com.google.mlkit:object-detection:17.0.2"
    )

// ==========================
// Coil
// ==========================

    implementation(
        "io.coil-kt:coil-compose:2.7.0"
    )

// ==========================
// Testing
// ==========================

    testImplementation(
        "junit:junit:4.13.2"
    )

    androidTestImplementation(
        "androidx.test.ext:junit:1.2.1"
    )

    androidTestImplementation(
        "androidx.test.espresso:espresso-core:3.6.1"
    )



}
