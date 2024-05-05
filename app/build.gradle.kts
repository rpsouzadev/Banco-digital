plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsKotlinAndroid)

  // Firebase
  id("com.google.gms.google-services")

  // Parcelize
  id("kotlin-parcelize")
  id("androidx.navigation.safeargs.kotlin")

  // Dagger hilt with KSP
  id("com.google.dagger.hilt.android")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.rpsouza.bancodigital"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.rpsouza.bancodigital"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)

  // Firebase
  implementation(platform(libs.firebase.bom))
  implementation("com.google.firebase:firebase-auth")
  implementation("com.google.firebase:firebase-database")

  // Dagger hilt with KSP
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)

  // Navigation
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)

  // Lifecycle
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.lifecycle.livedata.ktx)

  // Mask to editText https://github.com/VicMikhailau/MaskedEditText
  implementation("io.github.vicmikhailau:MaskedEditText:5.0.1")

  // Tests
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}