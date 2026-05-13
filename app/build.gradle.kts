plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {

    namespace = "com.example.ev_grama_charge"

    compileSdk = 36

    defaultConfig {

        applicationId = "com.example.ev_grama_charge"

        minSdk = 24

        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {

        viewBinding = true
    }
}

dependencies {

    // Core Android
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.appcompat)

    implementation(libs.material)

    implementation(libs.androidx.activity)

    implementation(libs.androidx.constraintlayout)

    // Firebase
    implementation(
        platform("com.google.firebase:firebase-bom:33.5.1")
    )

    implementation(
        "com.google.firebase:firebase-auth-ktx"
    )

    implementation(
        "com.google.firebase:firebase-firestore-ktx"
    )

    // Lifecycle
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5"
    )

    implementation(
        "androidx.lifecycle:lifecycle-livedata-ktx:2.8.5"
    )

    // Activity + Fragment
    implementation(
        "androidx.activity:activity-ktx:1.9.2"
    )

    implementation(
        "androidx.fragment:fragment-ktx:1.8.2"
    )

    // RecyclerView
    implementation(
        "androidx.recyclerview:recyclerview:1.3.2"
    )

    // CardView
    implementation(
        "androidx.cardview:cardview:1.0.0"
    )

    // ViewPager2
    implementation(
        "androidx.viewpager2:viewpager2:1.1.0"
    )

    // Material UI
    implementation(
        "com.google.android.material:material:1.12.0"
    )

    // Circle Image View
    implementation(
        "de.hdodenhof:circleimageview:3.1.0"
    )

    // Glide
    implementation(
        "com.github.bumptech.glide:glide:4.16.0"
    )

    // Lottie Animation
    implementation(
        "com.airbnb.android:lottie:6.4.0"
    )

    // Google Maps
    implementation(
        "com.google.android.gms:play-services-maps:18.2.0"
    )

    implementation(
        "com.google.android.gms:play-services-location:21.0.1"
    )

    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.5.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.5.1")

    // Testing
    testImplementation(
        "junit:junit:4.13.2"
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )
}