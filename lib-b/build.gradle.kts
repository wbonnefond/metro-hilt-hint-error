plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.metro)
}

android {
  namespace = "repro.libb"
  compileSdk = 36
  defaultConfig { minSdk = 24 }
}

metro { interop { includeHilt() } }

dependencies {
  // Runtime dependency only. Hilt's Gradle plugin and KSP processor aren't applied.
  implementation(libs.hilt.android)
}
