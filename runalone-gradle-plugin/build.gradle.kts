plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("runAlonePlugin") {
            id = "com.komamj.componentization.runalone"
            displayName = "runAlonePlugin"
            description = "The run alone plugin for componentization architecture."
            implementationClass =
                "com.komamj.componentization.runalone.plugin.RunAlonePlugin"
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    implementation(Dependencies.Plugin.ANDROID)
    implementation(Dependencies.Plugin.KOTLIN)
}
