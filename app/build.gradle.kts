plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.androidDaggerHilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    signingConfigs {
        create("release") {
        }
    }
    namespace = "com.example.blemeter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.blemeter"
        minSdk = 26
        targetSdk = 34
        versionCode = 211024
        versionName = "21.10.24"

        buildConfigField("int", "MIN_SDK_VERSION", "$minSdk")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    api(project(":feature:authentication"))
    api(project(":feature:wallet"))
    api(project(":core:navigation"))
    api(project(":core:designsystem"))
}