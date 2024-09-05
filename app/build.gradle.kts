plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.auditarrm.taxscan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.auditarrm.taxscan"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    packagingOptions {
        exclude ("META-INF/DEPENDENCIES")
        exclude ("META-INF/LICENSE")
        exclude ("META-INF/LICENSE.txt")
        exclude ("META-INF/NOTICE")
        exclude ("META-INF/INDEX.LIST")
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation (platform("com.google.cloud:libraries-bom:26.42.0"))
    implementation ("com.google.cloud:google-cloud-document-ai")
    implementation ("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("androidx.camera:camera-core:1.3.4")
    implementation ("androidx.camera:camera-camera2:1.3.4")
    implementation ("androidx.camera:camera-lifecycle:1.3.4")
    implementation ("androidx.camera:camera-video:1.3.4")
    implementation ("androidx.camera:camera-view:1.3.4")
    implementation ("androidx.camera:camera-extensions:1.3.4")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.itextpdf:itextg:5.5.10")
    implementation ("com.tom-roush:pdfbox-android:2.0.27.0")
}