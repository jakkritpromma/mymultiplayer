// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // Add the SonarQube plugin classpath here
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:4.0.0.2929")  // Latest version of SonarQube Gradle plugin
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}