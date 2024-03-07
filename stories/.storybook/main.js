module.exports = {
    stories: [
        "/Users/bitpogo/projects/antibytes/keather/stories/build/compileSync/js/main/productionExecutable/kotlin/*.stories.tsx"
    ],
    addons: [
        "@storybook/addon-links",
        "@storybook/addon-essentials",
        "@storybook/addon-onboarding",
        "@storybook/addon-interactions",
    ],
    framework: {
        name: "@storybook/react-webpack5",
        options: {
          builder: {
            useSWC: true,
          },
        },
    },
    docs: {
        autodocs: "tag",
    },
}
