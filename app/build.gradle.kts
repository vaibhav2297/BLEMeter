plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.androidDaggerHilt)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.google.service)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.example.blemeter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.blemeter"
        minSdk = 26
        targetSdk = 34
        versionCode = 250310
        versionName = "25.03.10"

        buildConfigField("int", "MIN_SDK_VERSION", "$minSdk")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    signingConfigs {
        create("release") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("C:\\Users\\vaibh\\.android\\debug.keystore")
            storePassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.lifecycle.runtime.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /*Dagger Hilt*/
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.kapt)

    /*Kotlin Serialization*/
    implementation(libs.ktx.serialization)

    /*Accompanist Permission Handling*/
    implementation(libs.accompanist.permissions)

    /*viewmodel*/
    implementation(libs.compose.viewmodel)
    implementation(libs.ktx.viewmodel)
    implementation(libs.dagger.hilt.navigation)

    implementation("com.juul.kable:core:0.31.1")
    
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    /*Lottie Animation*/
    implementation(libs.lottie.compose)

    //projects
    api(project(":core:payment"))
    api(project(":core:data:auth"))
    api(project(":core:logger"))
    api(project(":core:data:meter"))
    api(project(":feature:authentication"))
    api(project(":feature:wallet"))
    api(project(":core:navigation"))
    api(project(":core:designsystem"))

    //firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.crashlytics)
    api(libs.firebase.analytics)
}