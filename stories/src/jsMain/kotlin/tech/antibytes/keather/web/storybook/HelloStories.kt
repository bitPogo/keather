/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:OptIn(kotlin.js.ExperimentalJsExport::class)

package tech.antibytes.keather.web.storybook

import tech.antibytes.keather.web.MyComponent
import tech.antibytes.keather.web.MyComponent.MyComponent
import react.FC
import react.Props
import io.kvision.react.kvisionWrapper
import react.create

@JsExport
class HelloStories {
    val helloStory = kvisionWrapper {
        MyComponent("StoryBoard")
    }
}
