import React from 'react';
import { storiesOf } from '@storybook/react';
import * as Container from './keather-stories.js'

const stories = new Container.tech.antibytes.keather.web.storybook.HelloStories()

function SomeComponent() {
    console.log(stories)

    return (
        <div>HelloWorld</div>
    )
}

export const HelloMyWorld = {
  render: () => stories.helloStory(),
};

export default {
  // title: 'forms/RegistrationForm' -- optional
  component: stories.component,
};
