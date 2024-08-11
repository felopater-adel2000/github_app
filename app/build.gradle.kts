plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.felo.github_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.felo.github_app"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Koin DI
    implementation("io.insert-koin:koin-core:3.5.6")
    implementation("io.insert-koin:koin-android:3.5.6")

    //Navigation Dependency
    val nav_version = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-runtime-ktx:$nav_version")


    //logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    //Retrofit & Gson converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Swipe Refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Glide dependency
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")


    //sdp Dependency
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")

    //Google Services Auth
    implementation("com.google.android.gms:play-services-auth:20.5.0")

    //Facebook Auth
    implementation("com.facebook.android:facebook-android-sdk:17.0.1")

    //Material dialogs
    implementation("com.afollestad.material-dialogs:core:3.3.0")
    implementation("com.afollestad.material-dialogs:datetime:3.3.0")
    implementation("com.afollestad.material-dialogs:input:3.3.0")
}