module.exports = {
  chromeSelector: '#storybook-root > *',
  diffingEngine: 'pixelmatch',
  configurations: {
    'chrome.laptop': {
      target: 'chrome.app',
      width: 1366,
      height: 768,
    },
  },
  fetchFailIgnore: 'localhost:1234/get',
};
