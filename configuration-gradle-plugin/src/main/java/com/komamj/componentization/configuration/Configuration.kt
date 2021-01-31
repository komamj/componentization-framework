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

package com.komamj.componentization.configuration

import org.gradle.api.Action

open class Configuration {
    open val publication = PublicationOptions()
    open fun publication(action: Action<PublicationOptions>) {
        action.execute(publication)
    }

    open val jacoco: JacocoOptions = JacocoOptions()
    open fun jacoco(action: Action<JacocoOptions>) {
        action.execute(jacoco)
    }
}

open class JacocoOptions {
    open var isEnabled: Boolean = true
    open val excludes = mutableListOf<String>()
    open fun excludes(vararg excludes: String) {
        this.excludes.addAll(excludes)
    }
}

open class PublicationOptions {
    open var isEnabled: Boolean = true
    open var groupId = "com.komamj"
    open var artifactId = ""
    open var version = "0.0.1"
    open var url = ""
}
