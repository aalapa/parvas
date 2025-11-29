import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

// Auto-increment version
val versionPropsFile = file("../version.properties")
val versionProps = Properties()

if (versionPropsFile.canRead()) {
    versionProps.load(FileInputStream(versionPropsFile))
} else {
    throw GradleException("Could not read version.properties!")
}

fun getVersionCode(): Int {
    return versionProps.getProperty("versionCode", "1").toInt()
}

fun getVersionName(): String {
    val major = versionProps.getProperty("versionMajor", "1")
    val minor = versionProps.getProperty("versionMinor", "0")
    val patch = versionProps.getProperty("versionPatch", "0")
    return "$major.$minor.$patch"
}

fun incrementVersion() {
    val currentPatch = versionProps.getProperty("versionPatch", "0").toInt()
    val currentVersionCode = versionProps.getProperty("versionCode", "1").toInt()
    
    versionProps.setProperty("versionPatch", (currentPatch + 1).toString())
    versionProps.setProperty("versionCode", (currentVersionCode + 1).toString())
    
    versionProps.store(FileOutputStream(versionPropsFile), "Version auto-incremented on build")
    println("Version incremented to ${getVersionName()} (${currentVersionCode + 1})")
}

// Increment version on assembleRelease or assembleDebug
gradle.taskGraph.whenReady {
    if (allTasks.any { it.name.contains("assemble", ignoreCase = true) }) {
        incrementVersion()
    }
}

android {
    namespace = "com.aravind.parva"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aravind.parva"
        minSdk = 24
        targetSdk = 34
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    // Custom APK naming
    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val versionName = getVersionName()
            val buildType = buildType.name
            output.outputFileName = "parva-${versionName}-${buildType}.apk"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    
    // WorkManager for notifications
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    
    // Gson for JSON serialization
    implementation("com.google.code.gson:gson:2.10.1")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

