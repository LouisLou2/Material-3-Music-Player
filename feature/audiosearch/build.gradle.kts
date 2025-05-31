plugins {
    id("com.omar.android.feature")
    id("com.omar.android.compose")
    id("com.omar.android.hilt")
}

android {
    namespace = "com.omar.musica.audiosearch"
}

dependencies {
    // Core dependencies - 复用现有核心模块
    implementation(project(":core:ui"))
    implementation(project(":core:store"))
    implementation(project(":core:model"))
    implementation(project(":core:playback"))
    implementation(project(":core:network"))
    
    // Standard Android dependencies
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
} 