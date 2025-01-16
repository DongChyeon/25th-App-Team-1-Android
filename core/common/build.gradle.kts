import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.library")
    id("orbit.android.hilt")
}

android {
    setNamespace("core.common")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(libs.compose.navigation)
}
