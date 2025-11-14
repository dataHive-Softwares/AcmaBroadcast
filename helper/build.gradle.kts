plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "com.acma.broad.helper"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)

    // Core
    api(libs.acmabroadcastcore)
    api(libs.sdp.android)
    implementation(libs.shimmer)
    // Ads
    api(libs.play.services.ads)

    implementation(libs.kotlinx.serialization)


    // Use Compose BOM - Use 'api' to expose it to the app module
    api(platform(libs.androidx.compose.bom))

    // Compose Runtime
    api(libs.androidx.runtime)

    // Compose Foundation
    api(libs.androidx.foundation)
    api(libs.androidx.foundation.layout)

    // Preview support
    api(libs.androidx.ui.tooling.preview)

    // Debug-only tooling
    debugImplementation(libs.androidx.ui.tooling)

    // Other compose dependencies
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.material3)

}