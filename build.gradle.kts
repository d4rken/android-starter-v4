buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
        classpath("com.bugsnag:bugsnag-android-gradle-plugin:8.0.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean").configure {
    delete("build")
}