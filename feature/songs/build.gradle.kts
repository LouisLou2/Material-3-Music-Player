
plugins {
    id("com.omar.android.feature")
    id("com.omar.android.compose")
}

android {
    namespace = "com.omar.musica.songs"
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(project(mapOf("path" to ":core:store")))
    implementation(project(mapOf("path" to ":core:model")))
    implementation(project(mapOf("path" to ":core:ui")))
    implementation(project(mapOf("path" to ":core:playback")))
    implementation(project(":feature:audiosearch"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}