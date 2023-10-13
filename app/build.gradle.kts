plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.social_media_using_blockchain"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.social_media_using_blockchain"
        minSdk = 24
        targetSdk = 33
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    implementation ("de.hdodenhof:circleimageview:3.1.0") // to create circle images
    implementation("com.google.android.material:material:1.9.0") // material
    implementation("com.github.AtifSayings:Animatoo:1.0.1") // animations
    implementation ("com.github.bumptech.glide:glide:4.16.0") // glide means sliding
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")  //glide combined
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}