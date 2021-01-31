buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Dependencies.Plugin.ANDROID)
        classpath(Dependencies.Plugin.KOTLIN)
        classpath(Dependencies.Plugin.HILT)
        classpath(Dependencies.Plugin.AROUTER)
        classpath(Dependencies.Plugin.BINTRAY)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}