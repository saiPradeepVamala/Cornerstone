plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.cornerstonehospice"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cornerstonehospice"
        minSdk = 29
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    packaging{
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "mozilla/public-suffix-list.txt"
        }
    }
    buildToolsVersion = "34.0.0"
}


dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")

    implementation("com.google.code.gson:gson:2.11.0")
//   implementation(files("libs/android-logging-log4j-1.0.2.jar"))
//    replaced with the below as above is the old way of adding dependency
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
//    implementation("androidx.viewpager:viewpager:1.0.0")

//    Discontinued -> Not able to know what to add
//    implementation(files("libs/db4o-8.0.249.16098-all-java5.jar"))
//    Deprecated
//    implementation(files("libs/log4j-1.2.16.jar"))

//    implementation (files("libs/simple-xml-2.7.1.jar")) Replaced with below
    implementation("org.simpleframework:simple-xml:2.7.1")

//    implementation(files("libs/httpclient-4.1.1.jar"))
//    implementation(files("libs/httpclient-cache-4.1.1.jar"))
//    implementation(files("libs/httpcore-4.1.jar"))
//    implementation(files("libs/httpmime-4.1.1.jar"))
//    Replacing with new
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
    implementation("org.apache.httpcomponents:httpcore:4.4.16")
    implementation("org.apache.httpcomponents:httpmime:4.5.14")
    implementation("org.apache.httpcomponents:httpclient-cache:4.5.14")

//    Deprecated
//    implementation(files("libs/nineoldandroids-2.4.0.jar"))
    implementation("com.nineoldandroids:library:2.4.0")
//    implementation(files("libs/java-mail-1.4.4.jar"))
    implementation("javax.mail:mail:1.4.7")
//    implementation(files("libs/javax.activation.jar"))
    implementation("javax.activation:activation:1.1.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("org.testng:testng:6.9.6")
}
