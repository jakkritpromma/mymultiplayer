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
    namespace = "com.example.mymultiplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mymultiplayer"
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
    implementation("androidx.fragment:fragment-ktx:1.8.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.0")

    implementation("androidx.media3:media3-exoplayer:1.0.0")
    implementation("androidx.media3:media3-ui:1.0.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.0.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation("commons-net:commons-net:3.3")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0-rc03")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
