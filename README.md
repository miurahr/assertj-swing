# Asertj-Swing project

This project provides a simple and intuitive API for functional testing of Swing user interfaces, resulting in tests 
that are compact, easy to write, and read like a specification. Tests written using AssertJ Swing are also robust. 

AssertJ Swing simulates actual user gestures at the operating system level, ensuring that the application will behave correctly in 
front of the user. It also provides a reliable mechanism for GUI component lookup that ensures that changes in the GUI's 
layout or look-and-feel will not break your tests.

This project is a fork of https://gitub.com/assertj/assertj-swing , that is a fork of https://github.com/alexruiz/fest-swing-1.x.

## Document

You can check a [manual at readthedocs.org](https://assertj-swing.readthedocs.io/en/latest/)

## How to use

See [Getting Started](https://assertj-swing.readthedocs.io/en/latest/getting_started.html)


## Why forked?

AssertJ project forked the original project because a fest-swing project was stopped to maintain, and AssertJ project has been 
seeking a maintainer of assertj-swing project and stopped to maintain from Sep., 2020.
We got several warnings when using assertj-swing framework because of a lack of support for recent Java versions.

There are several forked projects that have individual improvements, and there have not been integrated yet.
Because it is important to keep dependencies secure and catch up new Java versions, I decided to fork it and integrate efforts.

## What are changed from the original?

### Supported platform

- Build with Java 11
- Supports Java 11, and Java 17
- Build with Gradle 8.12 instead of Maven

### Dependencies

- assertj-swing
    - assertj-core "3.27.4"
    - jetbrains_annotations "23.1.0"
- assertj-swing-junit
    - assertj-core "3.27.4"
    - commons_codec "1.17.1"
    - junit4 "4.13.2"
    - assertj-swing
    - fest-reflect
- assertj-swing-junit-jupiter
    - assertj-core "3.27.4"
    - junit-jupiter "5.9.3"
    - assertj-swing-junit
- fest-reflect
    - assertj-core "3.27.4"
- for unit tests
    - ant_junit "1.10.15"
    - mockito "5.18.0"
    - equals_verifier "2.5.2"
    - cglib "3.3.0"
- Bundle an updated `feat-reflect`

### testing

The project uses Xvfb virtual X server and fluxbox window manager for test.
These are automatically launched during a Gradle test task is running.

#### Handling failing tests

- By default, only the `assertj-swing` module ignores test failures (to allow investigation without breaking the build). Other modules will fail the build on test failures.
- You can review test results in the HTML reports generated per module, for example:
  - `assertj-swing/build/reports/tests/test/index.html`
  - `assertj-swing-junit/build/reports/tests/test/index.html`
  - `assertj-swing-junit-jupiter/build/reports/tests/test/index.html`
  - `fest-reflect/build/reports/tests/test/index.html`

To enforce failures globally (i.e., make all modules fail the build on test failures), run Gradle with one of the following flags:

```bash
./gradlew test -PenforceTestFailures=true
# or
./gradlew test -DenforceTestFailures=true
# or set environment variable (for the current shell)
ENFORCE_TEST_FAILURES=true ./gradlew test
```

### Java Platform Module System

These provide JPMS module definitions.
 
- `org.assertj.swing`
- `org.assertj.swing.junit`
- `org.assertj-swing.junit.jupiter`

### Changes and fixes

- Drop dependencies for `fest-assert`, `feat-test` and `feat-util`
- Change keystroke to use a key modifier extension such as `CTRL_DOWN_KEY` instead of KeyInput `CTRL_KEY`
- Drop JSR305 annotation and use JetBrains annotations
- Update unit tests with aseertj-core
  - Rewrite cases with Mockito instead of EasyMock
  - Rewrite cases with JUnit 4.13 instead of JUnit 3 and TestNG
- Import patches from the community
  - merge PortSwigger's changes  
  - merge ingokegel's Jdk17 brach
  - merge wjbakker's junit-jupiter extension
- Style
  - Update coding style by Spotless utility with AssertJ standard
  - Use space characters instead of tab character
  - Add config/eclipse/assertj-eclipse-formatter.xml
- Drop assertj-swing-jide
- test: Drop references for removed Swing peers in TookkitStub class
- test: Drop dependency for `multithreadedtc` and removal of some unit tests
  - ScreenLock_acquire_acquireedBy_release_Test
  - ScreenLock_acquire_Test
  - ScreenLock_getOwner_Test
- Release group ID is 'tokyo.northside'
- Add CI runner on codeberg-ci 
- Add a test runner script with docker

## How to build

The forked version of assrtj-swing uses Gradle for a build system. To build the framework, you need to use
Java SDK 17 to run Gradle.
The framework library will be built with Java SDK 11 and compatible with Java 11 and later.

### compile and test

```bash
./gradlew build
```

## License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
the License. You may get a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the 
specific language governing permissions and limitations under the License.
