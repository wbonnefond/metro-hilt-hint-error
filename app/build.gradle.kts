plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.metro)
}

android {
  namespace = "repro.app"
  compileSdk = 36
  defaultConfig {
    applicationId = "repro.app"
    minSdk = 24
    targetSdk = 36
  }
}

metro { interop { includeHilt() } }

dependencies {
  implementation(project(":lib-a"))
  implementation(project(":lib-b"))
  implementation(libs.hilt.android)
}
