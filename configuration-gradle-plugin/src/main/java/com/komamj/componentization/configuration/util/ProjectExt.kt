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

package com.komamj.componentization.configuration.util

import com.komamj.componentization.configuration.dependency.Dependencies
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

private const val IMPLEMENTATION = "implementation"
private const val KAPT = "kapt"

fun Project.addLifecycle(configurationName: String = IMPLEMENTATION) {
    dependencies {
        add(
            configurationName,
            Dependencies.AndroidX.LIFECYCLE
        )
        add(
            configurationName,
            Dependencies.AndroidX.LIFECYCLE_COMMON_JAVA8
        )
        add(
            configurationName,
            Dependencies.AndroidX.LIFECYCLE_VIEWMODEL
        )
        add(
            configurationName,
            Dependencies.AndroidX.LIFECYCLE_LIVE_DATA
        )
        add(
            configurationName,
            Dependencies.AndroidX.LIFECYCLE_SERVICE
        )
        add(
            configurationName,
            Dependencies.AndroidX.LIFECYCLE_PROCESS
        )
        add(
            configurationName,
            Dependencies.AndroidX.APPCOMPAT
        )
    }
}

fun Project.addConstraintLayout(configurationName: String = IMPLEMENTATION) {
    dependencies {
        add(
            configurationName,
            Dependencies.AndroidX.CONSTRAINT_LAYOUT
        )
    }
}

fun Project.addDaggerHilt(configurationName: String = IMPLEMENTATION) {
    dependencies {
        add(configurationName, Dependencies.Others.HILT_ANDROID)
        add(KAPT, Dependencies.Others.HILT_COMPILER)
    }
}

fun Project.addHiltAndroidX(configurationName: String = IMPLEMENTATION) {
    dependencies {
        add(configurationName, Dependencies.AndroidX.HILT_VIEWMODEL)
        add(KAPT, Dependencies.AndroidX.HILT_COMPILER)
    }
}

fun Project.addCoroutines(configurationName: String = IMPLEMENTATION) {
    dependencies {
        add(
            configurationName,
            Dependencies.Kotlin.COROUTINES_CORE
        )
        add(
            configurationName,
            Dependencies.Kotlin.COROUTINES_ANDROID
        )
    }
}