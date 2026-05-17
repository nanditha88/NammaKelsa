plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.nammakelsa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nammakelsa"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
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

    // CORE
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.1")

    // COMPOSE BOM (IMPORTANT)
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.compose.material3:material3")

    // 🔥 THIS FIXES YOUR ICON ERRORS
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.compose.foundation:foundation")

    // NAVIGATION
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // COIL (FIXED)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // FIREBASE (CLEAN ONLY BUNDLE ONCE)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // DEBUG
    debugImplementation("androidx.compose.ui:ui-tooling")
}