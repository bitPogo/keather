# Title
Short Title

[![Latest release](https://raw.githubusercontent.com/bitPogo/repo/main/docs/src/assets/badge-release-latest.svg)](https://github.com/bitPogo/repo/releases)
[![License](https://raw.githubusercontent.com/bitPogo/repo/main/docs/src/assets/badge-license.svg)](https://github.com/bitPogo/repo/blob/main/LICENSE)
[![Platforms](https://raw.githubusercontent.com/bitPogo/repo/main/docs/src/assets/badge-platform-support.svg)](https://github.com/bitPogo/repo/blob/main/docs/src/assets/badge-platform-support.svg)
[![CI - Build Snapshot Version](https://github.com/bitPogo/repo/actions/workflows/ci-snapshot.yml/badge.svg)](https://github.com/bitPogo/repo/actions/workflows/ci-snapshot.yml/badge.svg)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=repo&metric=coverage)](https://sonarcloud.io/summary/new_code?id=repo)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=repo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=repo)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=repo&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=repo)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/6023/badge)](https://bestpractices.coreinfrastructure.org/projects/6023)

## About The Project

### API Key
In order to be able to use the Api you need to set an API Key as environmental variables - `WEATHER_API`
To use them in Android studio please make sure you propagate both variables via `launchctl setenv`. See [here](https://issuetracker.google.com/issues/216364005) for more.

### Strange AndroidStudio Errors on sync

That is normal due to the iOS bindings. If you run the tasks by hand you see all is good.

### Don't forget to set permission

The app needs permission for geolocation.

### Known issues

* once running `./gradlew test` or `./gradlew check` the release tests will fail. While unfortunate you can simply ignore it with `-x testReleaseUnitTest`, since those tests do not contribute in particular. 

## Dependencies

## Additional Requirements

* Android 5.0 (API 21) to Android 14 (API 34)
* [Java 17](https://adoptopenjdk.net/?variant=openjdk17&jvmVariant=hotspot)

## Changelog

See [changelog](https://github.com/bitPogo/repo/blob/main/CHANGELOG.md).

## Versioning

This project uses [Semantic Versioning](http://semver.org/) as a guideline for our versioning.

## Contributing

You want to help or share a proposal? You have a specific problem? Read the following:

* [Code of Conduct](https://github.com/bitPogo/repo/blob/main/CODE_OF_CONDUCT.md) for details on our code of conduct.
* [Contribution Guide](https://github.com/bitPogo/repo/blob/main/CONTRIBUTING.md) for details about how to report bugs and propose features.

## Releasing

Please take a look [here](https://github.com/bitPogo/repo/tree/main/docs/src/development/releasing.md).

## Copyright and License

Copyright (c) 2024 Matthias Geisler / All rights reserved.

Please refer to the [License](https://github.com/bitPogo/repo/blob/main/LICENSE) for further details.
