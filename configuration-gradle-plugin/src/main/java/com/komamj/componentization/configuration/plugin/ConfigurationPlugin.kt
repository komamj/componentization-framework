/*
 * Copyright 2021 komamj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.komamj.componentization.configuration.plugin

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import com.diffplug.gradle.spotless.SpotlessExtension
import com.komamj.componentization.configuration.Configuration
import com.komamj.componentization.configuration.JacocoOptions
import com.komamj.componentization.configuration.PublicationOptions
import com.komamj.componentization.configuration.dependency.Dependencies
import com.komamj.componentization.configuration.dependency.Versions
import com.komamj.componentization.configuration.util.LICENCE_HEADER
import org.gradle.api.DomainObjectSet
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ConfigurationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("Configuration plugin apply.")
        project.plugins.apply(KOTLIN_ANDROID_PLUGIN)
        project.plugins.apply(SPOTLESS_PLUGIN)

        project.extensions.create(CUSTOM_CONFIGURATION, Configuration::class.java)

        // Configure common android build parameters.
        configureAndroidExtension(project)
        configureSpotless(project)
        configureDependencies(project)

        project.afterEvaluate {
            project.extensions.getByType(Configuration::class.java).run {
                val publicationsOptions = this.publication
                if (publicationsOptions.isEnabled && isLibrary(project)) {
                    configureMavenPublish(project, publicationsOptions)
                }
                val jacocoOptions = this.jacoco
                if (jacocoOptions.isEnabled) {
                    configureJacoco(project, jacocoOptions)
                }
            }
        }
    }

    private fun configureAndroidExtension(project: Project) {
        println("start configureAndroidExtension...")

        val androidExtension = project.extensions.getByName(ANDROID_EXTENSION)
        if (androidExtension is BaseExtension) {
            androidExtension.apply {
                compileSdkVersion(Versions.Android.COMPILE_SDK_VERSION)
                buildToolsVersion(Versions.Android.BUILD_TOOLS_VERSION)

                defaultConfig {
                    minSdkVersion(Versions.Android.MIN_SDK_VERSION)
                    targetSdkVersion(Versions.Android.TARGET_SDK_VERSION)
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                project.tasks.withType(KotlinCompile::class.java).configureEach {
                    kotlinOptions {
                        jvmTarget = Versions.Android.JVM_TARGET
                    }
                }

                // Configure common proguard file settings.
                configureProguardSettings()
            }
        }
    }

    private fun BaseExtension.configureProguardSettings() {
        when (this) {
            is LibraryExtension -> defaultConfig {
                consumerProguardFiles(LIBRARY_PROGUARD_FILES)
            }
            is AppExtension -> buildTypes {
                getByName(BUILD_TYPE_RELEASE) {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    isZipAlignEnabled = true
                    proguardFiles(
                        getDefaultProguardFile(DEFAULT_APPLICATION_PROGUARD_FILES),
                        APPLICATION_PROGUARD_FILES
                    )
                }
            }
        }
    }

    private fun configureSpotless(project: Project) {
        println("start configureSpotless...")

        val spotlessExtension = project.extensions.getByName(SPOTLESS_EXTENSION)
        if (spotlessExtension is SpotlessExtension) {
            spotlessExtension.apply {
                kotlin {
                    target("**/*.kt")
                    ktlint().userData(
                        mapOf("max_line_length" to "120")
                    )
                    licenseHeader(LICENCE_HEADER)
                }
            }
        }
    }

    private fun configureDependencies(project: Project) {
        project.dependencies {
            add(
                "implementation",
                Dependencies.Kotlin.STDLIB
            )
        }

        addTestDependencies(project)
    }

    private fun addTestDependencies(project: Project) {
        project.dependencies {
            add(
                "testImplementation",
                Dependencies.JunitTest.JUNIT
            )
        }
    }

    private fun configureMavenPublish(project: Project, publicationOptions: PublicationOptions) {
        println("start configure maven publish...")
        project.plugins.apply(MavenPublishPlugin::class.java)
        val publishingExtension = project.extensions.getByName(PublishingExtension.NAME)
        project.plugins.all {
            if (this is LibraryPlugin) {
                project.extensions.getByType(LibraryExtension::class.java).run {
                    libraryVariants.all {
                        val libraryVariantName = this.name.capitalize()
                        if (publishingExtension is PublishingExtension) {
                            publishingExtension.apply {
                                publications {
                                    create(libraryVariantName, MavenPublication::class.java) {
                                        from(project.components.findByName(libraryVariantName))
                                        this.groupId = publicationOptions.groupId
                                        this.artifactId = publicationOptions.artifactId
                                        this.version = publicationOptions.version
                                    }
                                }
                                repositories {
                                    maven {
                                        this.url =
                                            project.uri(
                                                if (publicationOptions.url.isEmpty()) {
                                                    "${project.rootProject.projectDir}/repos"
                                                } else publicationOptions.url
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureJacoco(project: Project, jacocoOptions: JacocoOptions) {
        project.plugins.apply(JacocoPlugin::class.java)
        project.plugins.all {
            when (this) {
                is LibraryPlugin -> {
                    project.extensions.getByType(LibraryExtension::class.java).run {
                        configureJacoco(project, libraryVariants, jacocoOptions)
                    }
                }
                is AppPlugin -> {
                    project.extensions.getByType(AppExtension::class.java).run {
                        configureJacoco(project, applicationVariants, jacocoOptions)
                    }
                }
            }
        }
    }

    private fun configureJacoco(
        project: Project,
        variants: DomainObjectSet<out BaseVariant>,
        options: JacocoOptions
    ) {
        variants.all {
            val variantName = name
            val isDebuggable = this.buildType.isDebuggable
            if (!isDebuggable) {
                project.logger.info("Skipping Jacoco for $name because it is not debuggable.")
                return@all
            }

            project.tasks.register<JacocoReport>("jacoco${variantName.capitalize()}Report") {
                dependsOn(project.tasks["test${variantName.capitalize()}UnitTest"])
                val coverageSourceDirs = "src/main/java"

                val javaClasses = project
                    .fileTree("${project.buildDir}/intermediates/javac/$variantName") {
                        setExcludes(options.excludes)
                    }

                val kotlinClasses = project
                    .fileTree("${project.buildDir}/tmp/kotlin-classes/$variantName") {
                        setExcludes(options.excludes)
                    }

                // Using the default Jacoco exec file output path.
                val execFile = "jacoco/test${variantName.capitalize()}UnitTest.exec"

                executionData.setFrom(
                    project.fileTree("${project.buildDir}") {
                        setIncludes(listOf(execFile))
                    }
                )

                // Do not run task if there's no execution data.
                setOnlyIf { executionData.files.any { it.exists() } }

                classDirectories.setFrom(javaClasses, kotlinClasses)
                sourceDirectories.setFrom(coverageSourceDirs)
                additionalSourceDirs.setFrom(coverageSourceDirs)

                reports.xml.isEnabled = true
                reports.html.isEnabled = true
            }
        }
    }

    private fun isLibrary(project: Project) =
        project.pluginManager.hasPlugin("com.android.library")

    companion object {
        private const val KOTLIN_ANDROID_PLUGIN = "kotlin-android"
        private const val SPOTLESS_PLUGIN = "com.diffplug.gradle.spotless"
        private const val SPOTLESS_EXTENSION = "spotless"
        private const val ANDROID_EXTENSION = "android"
        private const val CUSTOM_CONFIGURATION = "customConfiguration"
        private const val BUILD_TYPE_RELEASE = "release"
        private const val LIBRARY_PROGUARD_FILES = "consumer-rules.pro"
        private const val APPLICATION_PROGUARD_FILES = "proguard-rules.pro"
        private const val DEFAULT_APPLICATION_PROGUARD_FILES = "proguard-android-optimize.txt"
    }
}
