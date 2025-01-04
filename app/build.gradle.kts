plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.sonarqube")  // Ensure this plugin is included
}

sonarqube {
    properties {
        property("sonar.projectKey", "your_project_key") // Replace with your actual project key
        property("sonar.organization", "your_organization") // Replace with your organization (for SonarCloud users)
        property("sonar.host.url", "http://localhost:9000") // Replace with your SonarQube server URL
        property("sonar.login", "your_sonarqube_token") // Replace with your actual SonarQube token
        property("sonar.kotlin.language.level", "1.8") // Kotlin version you're using (adjust if necessary)
    }
}

android {
    namespace = "com.jakkagaku.mymultiplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jakkagaku.mymultiplayer"
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    lintOptions {
        // Enable XML report generation for SonarQube integration
        xmlReport = true
        xmlOutput = file("$buildDir/reports/lint-results.xml")  // Corrected line
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.picasso:picasso:2.8")

    // fragments
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")

    implementation("androidx.media3:media3-exoplayer:1.5.0")
    implementation("androidx.media3:media3-ui:1.5.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.5.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.4")

    implementation("commons-net:commons-net:3.3")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    val composeBom = platform("androidx.compose:compose-bom:2024.09.02")
    implementation(composeBom)
            androidTestImplementation(composeBom)

            // Choose one of the following:
            // Material Design 3
            implementation("androidx.compose.material3:material3")
    // or Material Design 2
    implementation("androidx.compose.material:material")
    // or skip Material Design and build directly on top of foundational components
    implementation("androidx.compose.foundation:foundation")
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation("androidx.compose.ui:ui")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    implementation("androidx.compose.material3:material3-window-size-class")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.9.3")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")

    implementation("com.google.accompanist:accompanist-themeadapter-material3:0.33.2-alpha")

    implementation("io.coil-kt:coil-compose:2.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
