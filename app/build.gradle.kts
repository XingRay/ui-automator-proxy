plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.github.xingray.uiautomatorproxy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.xingray.uiautomatorproxy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
        compose = false
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout.constraintlayout)
    implementation(libs.com.google.android.material.material)
    implementation(libs.androidx.recyclerview.recyclerview)
    implementation(libs.androidx.viewpager2)
    implementation(libs.glide)

    // Android Test dependencies
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.uiautomator)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // test
    testImplementation(libs.junit)

    // https://github.com/google/gson
    implementation(libs.gson)

    // jackson
    implementation(libs.jackson.databind)
    implementation(libs.jackson.dataformat.xml)
    implementation(libs.jackson.module.kotlin)
    // 提供 JavaTimeModule() 以支持 java.time.* 类型
    implementation(libs.jackson.datatype.jsr310)

    // ktor
    // core
    implementation(libs.ktor.server.core)
    // netty
    implementation(libs.ktor.server.netty)
    // log
    implementation(libs.ktor.server.call.logging)
    implementation(libs.logback.classic)

    // 返回值序列化
    implementation(libs.ktor.server.content.negotiation)
    // json序列化库, 3选1
    //implementation(libs.ktor.serialization.kotlinx.json)
    //implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.serialization.jackson)
    // xml 序列化
//    implementation(libs.ktor.serialization.kotlinx.xml)
    // cbor
//    implementation(libs.ktor.serialization.kotlinx.cbor)
    // protobuf
//    implementation(libs.ktor.serialization.kotlinx.protobuf)

    implementation("com.github.xingray:kotlin-base:0.0.3")
}