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

object Versions {
    object Android {
        const val COMPILE_SDK_VERSION = 30
        const val BUILD_TOOLS_VERSION = "30.0.3"
        const val MIN_SDK_VERSION = 21
        const val TARGET_SDK_VERSION = 30
        const val JVM_TARGET = "1.8"
    }

    object Plugin {
        const val ANDROID_GRADLE_PLUGIN = "4.1.2"
        const val KOTLIN = "1.4.21"
        const val HILT = "2.28.3-alpha"
        const val ROUTER = "1.0.2"
        const val BINTRAY = "1.8.5"
    }

    object Kotlin {
        const val KOTLIN = "1.4.21"
        const val COROUTINES = "1.3.9"
    }

    object AndroidX {
        const val ANNOTATION = "1.1.0"
        const val BIOMETRIC = "1.2.0-alpha01"
        const val MULTIDEX = "2.0.1"
        const val LIFECYCLE = "2.2.0"
        const val APPCOMPAT = "1.2.0"
        const val FRAGMENT = "1.2.5"
        const val MATERIAL = "1.2.1"
        const val CONSTRAINT_LAYOUT = "2.0.4"
        const val VIEWPAGER2 = "1.0.0"
        const val WEBKIT = "1.3.0"
        const val NAVIGATION = "2.3.1"
        const val ROOM = "2.2.5"
        const val HILT = "1.0.0-alpha02"
        const val WORK = "2.4.0"
        const val STARTUP = "1.0.0"
        const val PAGING = "3.0.0-alpha10"
        const val DATASTORE = "1.0.0-alpha03"
        const val SQLITE = "2.0.1"
    }

    object Others {
        const val PERMISSION = "3.0.0"
        const val TIMBER = "4.7.1"
        const val ROUTER = "1.5.1"
        const val ROUTER_COMPILER = "1.2.2"
        const val GSON = "2.8.6"
        const val GLIDE = "4.9.0"
        const val RETROFIT = "2.9.0"
        const val OKHTTP = "4.7.2"
        const val OKHTTP_LOGGING_INTERCEPTOR = "4.7.2"
        const val OKIO = "2.9.0"
        const val LOTTIE = "3.4.1"
        const val HILT = "2.28.3-alpha"
        const val SQLCIPHER = "4.4.2"
    }

    object JunitTest {
        const val JUNIT = "4.13.1"
        const val MOCKITO_CORE = "3.5.11"
        const val TRUTH = "1.0"
        const val LIFECYCLE = "2.1.0"
        const val PAGING_COMMON = "3.0.0-alpha10"
    }

    object AndroidTest {
        const val JUNIT = "1.1.2"
        const val ESPRESSO_CORE = "3.3.0"
        const val NAVIGATION = "2.3.2"
    }
}