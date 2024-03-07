import React from 'react';
import * as Container from './stories.js'

const stories = new Container.storybook.playground.HelloStories()

export default {
    title: stories.title,
    component: stories.component,
}

export const helloStory = stories.helloStory
