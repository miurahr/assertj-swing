# AssertJ Swing

AssertJ Swing is a Java library that provides a fluent interface for functional Swing UI testing.

AssertJ Swing is based on JDK standard types assertions and can be used with JUnit4 or JUnit5.

### Features of (forked) AssertJ Swing

- [Simulation of user interaction](assertj-swing-input.md) with a GUI (e.g. drag 'n drop)
- Reliable [GUI component lookup](assertj-swing-lookup.md) (by type, by name or custom search criteria)
- Support for all Swing components included in the JDK
- Compact and powerful API for creation and maintenance of functional GUI tests
- Supports [Applet testing](assertj-swing-launch.md#applets)
- Ability to embed [screenshots of failed GUI tests](assertj-swing-troubleshooting-screenshots.md) in HTML test reports
- Can be used with either [JUnit4](https://junit.org/junit4/) or [JUnit5](https://junit.org/junit5/)
- Supports [testing violations](assertj-swing-edt.md#Testing-that-access-to-Swing-components-is-done-in-the-EDT) of Swing's threading rules

## Getting started

To get quickly started, just have a look at our [one minute starting guide](assertj-swing-quick-start.md)

 If you have a bit more time, start with our [getting started guide](assertj-swing-getting-started.md)

### Why have I forked Fest Swing?

AssertJ Swing is a fork of FEST Swing, a great project I have used for five years and that Joel has contributed
to during 3 years, so why have I forked it?

 Well the main reason is, FEST Swing isn't really being continued. On 10th September, 2012
 [Alex announced](https://groups.google.com/d/msg/easytesting/bAt1nWx55FM/e3DF0bxGUf8J)
that he stopped development and that Yvonne is going to continue FEST Swing. Unfortunately except February
and March of 2013 there were no further commit activities on FEST Swing.

I think even if some developers declare Swing as dead, FEST Swing is still used a lot and the
development should continue. Not only support for the next Java releases should be ensured. But there
are also a lot of issues that would improve FEST Swing. Therefore we added AssertJ Swing to the
AssertJ suite.

__Joel Costigliola (AssertJ creator) and Christian R&ouml;sch (maintainer of AssertJ Swing)__

## Code and issue tracker

- The forked AssertJ Swing is hosted on https://github.com/miurahr/assertj-swing

- Please report bugs or missing features in [AssertJ Swing issue tracker](https://github.com/miurahr/assertj-swing/issues)

## Contributing to AssertJ Swing

You are very welcome to contribute, we really want to offer the richer and easy to use Swing testing API,
so ideas from our users are very appreciated.

Contributing is easy and we try to help people contributing, have a look at the contributor guidelines
(these are the same guidelines shown when you create a new issue).

 Special thanks to all assertj-swing contributors:

- [stefanmahler](https://github.com/stefanmahler)
- [ingokegel](https://github.com/ingokegel)

## Thanks

AssertJ Swing has its roots in FEST Swing, a project I have used for 5 years, so thanks to Alex Ruiz FEST's creator!

This documentation is based on the large documentation of FEST Swing. So thanks a lot to all the original authors!