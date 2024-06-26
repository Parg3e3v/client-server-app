plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.daggerHiltAndroid)
    alias(libs.plugins.devtoolsKsp)
    kotlin("kapt")
}

android {
    namespace = "com.parg3v.data"
    compileSdk = 34

    packaging {
        resources.excludes.add("META-INF/*")
    }

    hilt {
        enableAggregatingTask = true
    }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Ktor
    implementation (libs.ktor.client.core)
    implementation (libs.ktor.client.okhttp)
    implementation (libs.ktor.client.websockets)

    // Ktor Server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.websockets)
    implementation (libs.ktor.server.netty)
    implementation (libs.ktor.server.host.common)
    implementation (libs.ktor.server.status.pages)

    // Serialization
    implementation (libs.kotlinx.serialization.json)


    // Dagger-Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation (libs.androidx.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    annotationProcessor(libs.androidx.room.room.compiler)
    ksp(libs.androidx.room.room.compiler)

    //Data Store
    implementation (libs.androidx.datastore.preferences)

}