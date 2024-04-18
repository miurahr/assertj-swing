# Asertj-Swing project

This project provides a simple and intuitive API for functional testing of Swing user interfaces, resulting in tests 
that are compact, easy to write, and read like a specification. Tests written using AssertJ Swing are also robust. 

AssertJ Swing simulates actual user gestures at the operating system level, ensuring that the application will behave correctly in 
front of the user. It also provides a reliable mechanism for GUI component lookup that ensures that changes in the GUI's 
layout or look-and-feel will not break your tests.

This project is a fork of https://gitub.com/assertj/assertj-swing , that is a fork of https://github.com/alexruiz/fest-swing-1.x.

## Why forked?

AssertJ project forked the original project because a fest-swing project was stopped to maintain, and AssertJ project has been 
seeking a maintainer of assertj-swing project, and stopped to maintain from Sep., 2020.
We got several warnings when using assertj-swing framework because of a lack of support for recent Java versions.

There are several forked projects which have individual improvements and there have not been integrated yet.
OmegaT project uses assertj-swing-junit for testing GUI parts, especially for a vldocking library and plan to test OmegaT
itself in the future.

Because it is important to keep dependencies secure, and catch up new Java versions, we decided to fork it
and integrate efforts into our repository.


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
the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the 
specific language governing permissions and limitations under the License.
